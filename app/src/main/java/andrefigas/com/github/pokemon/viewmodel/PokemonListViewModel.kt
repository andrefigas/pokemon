package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.injection.modules.NetworkModule
import andrefigas.com.github.pokemon.model.entities.BaseEntity
import andrefigas.com.github.pokemon.model.entities.Pokemon
import andrefigas.com.github.pokemon.model.entities.PokemonListDataModel
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
import io.reactivex.schedulers.Schedulers
import repository.entities.ResultPage
import javax.inject.Inject

class PokemonListViewModel @Inject constructor(private val networkModule: NetworkModule) :
    ViewModel() {

    private var disposable: Disposable? = null
    private val liveData = MutableLiveData<PokemonListDataModel>()

    init {
        liveData.value = PokemonListDataModel()
    }

    private fun <T> configObserver(view: T) where  T : MainActivityContract, T : LifecycleOwner {
        if (liveData.hasObservers()) {
            return
        }

        liveData.observe(view, Observer { model ->

            if (!model.initted) {
                return@Observer
            }

            if (model.failed) { //failure

                if (model.previous == null) {//first request
                    view.hideStartingDataProgress()
                    view.showInitialLoadDataError()
                } else {//next request
                    view.hideIncreasingDataProgress()
                    view.showIncreasingDataDataError()
                    view.disableInfinityScroll()
                }

            } else { //success

                if (model.currentUrl == null) {//first request
                    view.hideStartingDataProgress()
                    view.createPokemonList()
                } else {//next request
                    view.hideIncreasingDataProgress()
                }

                view.increasePokemonList(model.pokemons)
            }


        })
    }

    private fun <T> configView(view: T) where  T : MainActivityContract, T : LifecycleOwner {
        if (providePokemonListDataModel().nextUrl == null) {
            view.showStartingDataProgress()
        } else {
            view.showIncreasingDataProgress()
        }

    }

    private fun providePokemonListDataModel(): PokemonListDataModel {
        return liveData.value as PokemonListDataModel
    }

    fun <T> fetchData(
        context: Context,
        view: T
    ) where  T : MainActivityContract, T : LifecycleOwner {

        view.hideInitialLoadDataError()

        if (providePokemonListDataModel().nextUrl == null && providePokemonListDataModel().previous != null) {
            return
        }

        configObserver(view)
        configView(view)

        val apiClient: ApiClient = networkModule.provideApiClient(context)

        val url = providePokemonListDataModel().nextUrl

        val resultPage: Single<ResultPage<BaseEntity>> = if (url == null)
            apiClient.fetchPokemons() else //first request
            apiClient.fetchPokemons(url) //next requests

        //getting pokemon list (each item contains just name and url)
        disposable = resultPage
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { resultPage ->
                //getting data details for each pokemon
                fetchPokemonsForPage(context, resultPage, url)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                liveData.value = it
            }, {
                //error
                val model = liveData.value
                model?.initted = true
                model?.failed = true
                liveData.value = model
            })

    }

    fun fetchPokemonsForPage(
        context: Context,
        resultPage: ResultPage<BaseEntity>,
        url: String?
    ): Single<PokemonListDataModel> {
        return Single.zip(resultPage.results.map { baseEntity ->
            networkModule.provideApiClient(context).getPokemon(baseEntity.url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map { pokemon ->
                    pokemon.name = baseEntity.name
                    pokemon.url = baseEntity.url
                    pokemon
                }
        }) { pokemonPages ->
            pokemonPages.map { pokemon ->
                pokemon as Pokemon
            }
        }.map { pokemons ->
            val previousItems = providePokemonListDataModel().pokemons.toMutableList()
            previousItems.addAll(pokemons)
            PokemonListDataModel(
                previousItems,
                resultPage,
                url
            )
        }

    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun isProgressing(): Boolean {
        return !(disposable?.isDisposed ?: true)
    }

}