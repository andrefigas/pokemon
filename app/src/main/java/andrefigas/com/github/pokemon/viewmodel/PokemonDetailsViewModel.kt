package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.injection.modules.NetworkModule
import andrefigas.com.github.pokemon.model.entities.FavoritePokemon
import andrefigas.com.github.pokemon.model.entities.FavoriteResponse
import andrefigas.com.github.pokemon.model.entities.Pokemon
import andrefigas.com.github.pokemon.model.entities.Specie
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PokemonDetailsViewModel @Inject constructor(private val networkModule: NetworkModule) :
    ViewModel() {

    private val specieLiveData = MutableLiveData<Specie>()
    private val favoriteLiveData = MutableLiveData<FavoriteResponse>()

    var fetchDisposable : Disposable? = null
    var updateDisposable : Disposable? = null
    var imageDisposable: coil.request.Disposable? = null

    lateinit var pokemon: Pokemon

    fun <T> updateFavorite(
        context: Context,
        favorite : Boolean,
        view: T
    ) where  T : DetailsActivityContract, T : LifecycleOwner {
        updateDisposable = networkModule.provideWebHookClient(context)
            .updateFavoritePokemon(FavoritePokemon(pokemon.id, favorite))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
            Consumer {
                if(favorite){
                    view.showAddFavoriteUpdateSuccess(pokemon.name.capitalize())
                }else{
                    view.showRemoveFavoriteUpdateSuccess(pokemon.name.capitalize())
                }
            }, Consumer {
                it.printStackTrace()
            })
    }

    fun <T> fetchData(
        context: Context,
        intent: Intent,
        view: T
    ) where  T : DetailsActivityContract, T : LifecycleOwner {

        val pokemon = IntentArgsUtils.getPokemonByArgs(intent) ?: return

        this.pokemon = pokemon

        bindPreloadedInformation(context, view, pokemon)

        val specieUrl: String = pokemon.species?.url ?: return

        view.showStartingDataProgress()

        specieLiveData.observe(view, Observer { specie ->

            view.hideStartingDataProgress()

            val description: String? = getDescription(specie)
            if (description != null) {
                view.showPokemonDescription(description.replace("\n", " ", false))
            }

            view.showHabitat(specie.habitat?.name ?: "")

        })

        favoriteLiveData.observe(view, Observer {favoriteResponse ->
            if(favoriteResponse.favorite){
                view.showFavoriteChecked()
            }else{
                view.showFavoriteUnchecked()
            }

        })

        fetchDisposable = networkModule.provideApiClient(context).getSpecie(specieUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .pair(networkModule.provideWebHookClient(context).getFavoriteByPokemon(pokemon.id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()))
            .subscribe ({ pair->
                specieLiveData.value = pair.first
                favoriteLiveData.value = pair.second
            }, {
                it.stackTrace
            })

    }

    private fun getDescription(specie: Specie): String? {

        val defLanguage = "en"

        return specie.labels.firstOrNull { it.language?.name == defLanguage }?.label
    }

    private fun bindPreloadedInformation(
        context: Context,
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

        if(moves != null && moves.isNotEmpty()){
            view.showPokemonMoves(moves)
        }

        bindImage(context, view, pokemon)
    }

    private fun bindImage(context: Context, view: DetailsActivityContract, pokemon: Pokemon) {
        imageDisposable = ImageUtils.loadPokemonImage(context, pokemon, object : Target {
            override fun onSuccess(result: Drawable) {
                super.onSuccess(result)
                view.showPokemonImage(result)
            }

            override fun onError(error: Drawable?) {
                super.onError(error)
                if(error != null){
                    view.showPokemonErrorImage(error)
                }

            }
        })

    }

    override fun onCleared() {
        super.onCleared()
        fetchDisposable?.dispose()
        imageDisposable?.dispose()
    }

    fun isUpdateProgressing() : Boolean{
        return !(updateDisposable?.isDisposed ?: true)
    }

}