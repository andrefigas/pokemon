package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.injection.modules.NetworkModule
import andrefigas.com.github.pokemon.model.entities.*
import andrefigas.com.github.pokemon.utils.ImageUtils
import andrefigas.com.github.pokemon.utils.IntentArgsUtils
import andrefigas.com.github.pokemon.utils.pair
import andrefigas.com.github.pokemon.view.details.DetailsActivityContract
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import coil.target.Target
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

open class PokemonDetailsViewModel @Inject constructor(private val networkModule: NetworkModule) :
    ViewModel() {

    companion object {
        const val DEFAULT_LANG = "en"
    }

    private val pokeDetailsDataModel = MutableLiveData<PokemonDetailsDataModel>()
    private val updateFavoriteResponse = MutableLiveData<UpdateFavoriteResponse>()

    var fetchDisposable: Disposable? = null
    var updateDisposable: Disposable? = null
    var imageDisposable: coil.request.Disposable? = null
    lateinit var pokemon: Pokemon

    fun <T> fetchData(
        context: Context?,
        intent: Intent,
        view: T
    ) where  T : DetailsActivityContract, T : LifecycleOwner {

        view.hideAllFields()

        val pokemon = IntentArgsUtils.getPokemonByArgs(intent) ?: return

        this.pokemon = pokemon

        pokeDetailsDataModel.value = PokemonDetailsDataModel(pokemon)

        bindPreloadedInformation(context, view, pokemon)

        val specieEntity: BaseEntity = pokemon.species ?: return

        view.showStartingDataProgress()
        view.hideAllFields()

        Locale.getDefault().language

        updateFavoriteResponse.observe(view, Observer { updateFavoriteResponse ->
            if (updateFavoriteResponse.error) {

                view.toggleFavoriteCheck()

                if (updateFavoriteResponse.favorite) {
                    view.showErrorOnAddFavorite(pokeDetailsDataModel.value?.pokemon?.name ?: "")
                } else {
                    view.showErrorOnRemoveFavorite(pokeDetailsDataModel.value?.pokemon?.name ?: "")
                }


            } else if (updateFavoriteResponse.favorite) {
                view.showAddFavoriteUpdateSuccess(pokemon.name.capitalize())
            } else {
                view.showRemoveFavoriteUpdateSuccess(pokemon.name.capitalize())
            }
        })

        pokeDetailsDataModel.observe(view, Observer { model ->
            if (!model.initted) {
                return@Observer
            }

            view.hideStartingDataProgress()

            val specie = model.species

            if (specie == null) {
                view.showPreloadedFields()
                return@Observer
            }

            val description: String? = getDescription(specie)
            if (description != null) {
                view.showPokemonDescription(description.replace("\n", " ", false))
            }

            view.showHabitat(specie.habitat?.name ?: "")
            view.showAllFields()

            val favoriteResponse = model.favoriteResponse ?: return@Observer

            if (favoriteResponse.favorite) {
                view.showFavoriteChecked()
            } else {
                view.showFavoriteUnchecked()
            }

        })

        fetchDisposable = fetchData(context, provideUrl(specieEntity))
            .subscribe { pair ->
                val pokemonDetailsDataModel = PokemonDetailsDataModel(pokemon)
                pokemonDetailsDataModel.initted = true
                pokemonDetailsDataModel.species = pair.first
                pokemonDetailsDataModel.favoriteResponse = pair.second
                pokeDetailsDataModel.value = pokemonDetailsDataModel
            }

    }

    fun fetchData(
        context: Context?,
        specieUrl: String,
        subscribeOn: Scheduler? = Schedulers.io(),
        observeOn: Scheduler? = AndroidSchedulers.mainThread()
    ): Single<Pair<Specie?, FavoriteResponse?>> {

        var specieRequest: Single<Specie?> =
            networkModule.provideApiClient(context).getSpecie(specieUrl)
        var favoriteRequest: Single<FavoriteResponse?> =
            networkModule.provideWebHookClient(context).getFavoriteByPokemon(pokemon.id)

        if (subscribeOn != null) {
            specieRequest = specieRequest.subscribeOn(subscribeOn)
            favoriteRequest = favoriteRequest.subscribeOn(subscribeOn)
        }

        if (observeOn != null) {
            specieRequest = specieRequest.observeOn(observeOn)
            favoriteRequest = favoriteRequest.observeOn(observeOn)
        }

        return specieRequest
            .pair(favoriteRequest)
            .onErrorReturnItem(Pair<Specie?, FavoriteResponse?>(null, null))
    }

    private fun getDescription(specie: Specie): String? {
        val defLanguage = DEFAULT_LANG
        return specie.labels.firstOrNull { it.language?.name == defLanguage }?.label
    }

    private fun bindPreloadedInformation(
        context: Context?,
        view: DetailsActivityContract,
        pokemon: Pokemon
    ) {

        val types = pokemon.types?.mapNotNull { it.content?.name }
        if (types != null && types.isNotEmpty()) {
            view.showTypes(types)
        }

        view.showPokemonName(pokemon.name.capitalize())

        view.showPokemonWeight(pokemon.weight)

        view.showPokemonHeight(pokemon.height)

        val moves = pokemon.moves?.map { it.content?.name }?.filterNotNull()

        if (moves != null && moves.isNotEmpty()) {
            view.showPokemonMoves(moves)
        }

        bindImage(context, view, pokemon)
    }

    fun updateFavorite(
        context: Context,
        favorite: Boolean
    ) {

        updateDisposable = updateFavoriteRequest(context, favorite)
            .subscribe(
                {
                    updateFavoriteResponse.value =
                        UpdateFavoriteResponse(it.message, favorite, false)
                }, {
                    updateFavoriteResponse.value = UpdateFavoriteResponse("", favorite, true)
                })
    }

    fun updateFavoriteRequest(
        context: Context?,
        favorite: Boolean,
        subscribeOn: Scheduler? = Schedulers.io(),
        observeOn: Scheduler? = AndroidSchedulers.mainThread()
    ): Single<UpdateFavoriteResponse> {

        var request = networkModule.provideWebHookClient(context)
            .updateFavoritePokemon(FavoritePokemon(pokemon.id, favorite))
            .map { updateFavoriteResponse ->
                updateFavoriteResponse.favorite = favorite
                updateFavoriteResponse
            }

        if (subscribeOn != null) {
            request = request.subscribeOn(subscribeOn)
        }

        if (observeOn != null) {
            request = request.observeOn(observeOn)
        }

        return request

    }


    open fun provideUrl(specie: BaseEntity): String {
        return specie.url
    }

    private fun bindImage(context: Context?, view: DetailsActivityContract, pokemon: Pokemon) {
        if (context == null) {
            return
        }

        imageDisposable = ImageUtils.loadPokemonImage(context, pokemon, object : Target {
            override fun onSuccess(result: Drawable) {
                super.onSuccess(result)
                view.showPokemonImage(result)
            }

            override fun onError(error: Drawable?) {
                super.onError(error)
                if (error != null) {
                    view.showPokemonErrorImage(error)
                }

            }
        })

    }

    override fun onCleared() {
        super.onCleared()
        fetchDisposable?.dispose()
        imageDisposable?.dispose()
        updateDisposable?.dispose()
    }

    fun isUpdateProgressing(): Boolean {
        return !(updateDisposable?.isDisposed ?: true)
    }

}