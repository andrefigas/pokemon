package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.injection.modules.NetworkModule
import andrefigas.com.github.pokemon.model.entities.BaseEntity
import andrefigas.com.github.pokemon.model.entities.Pokemon
import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import andrefigas.com.github.pokemon.view.main.MainActivityContract
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import repository.entities.ResultPage
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PokemonListViewModel @Inject constructor(private val networkModule: NetworkModule) :
    ViewModel() {
    private var disposable: Disposable? = null
    private val livedata = MutableLiveData<MutableList<Pokemon>>()
    private var nextUrl: String? = null
    private var previous: String? = null

    fun <T> configView(view: T) where  T : MainActivityContract, T : LifecycleOwner {
        val firstRequest = nextUrl == null
        if(firstRequest){
            view.showStartingDataProgress()
            view.createPokemonList()
            livedata.observe(view, Observer { pokemons ->
                if (firstRequest) {
                    view.hideStartingDataProgress()
                }else{
                    view.hideIncreasingDataProgress()
                }

                view.increasePokemonList(pokemons)
            })
        }else{
            view.showIncreasingDataProgress()
        }

    }

    fun <T> fetchData(context: Context, view: T) where  T : MainActivityContract, T : LifecycleOwner {

        configView(view)

        val apiClient : ApiClient =  networkModule.provideApiClient(context)

        val limit = "10"
        val offset = "0"

        val url = nextUrl

        val resultPage : Single<ResultPage<BaseEntity>> = if (url == null)
            apiClient.fetchPokemons(limit, offset) else //first request
            apiClient.fetchPokemons(url) //next requests

        //getting pokemon list (each item cotains just name and url)
        disposable =  resultPage
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                nextUrl = it.next
                previous = it.previous

                val requests = it.results.map { baseEntity ->
                    networkModule.provideApiClient(context).getPokemon(baseEntity.url)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).map { pokemon ->
                            Pokemon(
                                baseEntity.name,
                                baseEntity.url,
                                pokemon.weight,
                                pokemon.height,
                                pokemon.spritesCollection,
                                pokemon.species,
                                pokemon.types,
                                pokemon.moves
                            )
                        }
                }

                //getting data details for each pokemon
                Single.zip(requests) { pokemonPages ->
                    pokemonPages.map { pokemon ->
                        pokemon as Pokemon
                    }
                }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                livedata.value = it.toMutableList()
            })

    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun isProgressing() : Boolean{
        return !(disposable?.isDisposed ?: true)
    }

}