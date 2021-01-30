package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.injection.modules.NetworkModule
import andrefigas.com.github.pokemon.model.entities.Pokemon
import andrefigas.com.github.pokemon.model.entities.Specie
import andrefigas.com.github.pokemon.utils.ImageUtils
import andrefigas.com.github.pokemon.utils.IntentArgsUtils
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
import javax.inject.Inject

class PokemonDetailsViewModel @Inject constructor(private val networkModule: NetworkModule) :
    ViewModel() {

    val livedata = MutableLiveData<Specie>()
    var specieDisposable: Disposable? = null
    var imageDisposable: coil.request.Disposable? = null

    fun <T> fetchData(
        context: Context,
        intent: Intent,
        view: T
    ) where  T : DetailsActivityContract, T : LifecycleOwner {

        val pokemon = IntentArgsUtils.getPokemonByArgs(intent) ?: return

        bindPreloadedInformation(context, view, pokemon)

        val specieUrl: String = pokemon.species?.url ?: return

        view.showStartingDataProgress()

        livedata.observe(view, Observer { specie ->

            view.hideStartingDataProgress()

            val description: String? = getDescription(specie)
            if (description != null) {
                view.showPokemonDescription(description.replace("\n", " ", false))
            }

            view.showHabitat(specie.habitat?.name ?: "")

        })

        specieDisposable = networkModule.provideApiClient(context).getSpecie(specieUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer { specie ->
                livedata.value = specie
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
        })

    }

    override fun onCleared() {
        super.onCleared()
        specieDisposable?.dispose()
        imageDisposable?.dispose()
    }
}