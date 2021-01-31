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
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import repository.entities.ResultPage
import javax.inject.Inject

open class PokemonListViewModel @Inject constructor(private val networkModule: NetworkModule) :
    ViewModel() {

    private var disposable: Disposable? = null
    private val liveData = MutableLiveData<PokemonListDataModel>()

    private fun <T> configObserver(view: T) where  T : MainActivityContract, T : LifecycleOwner {
        if (liveData.hasObservers()) {
            return
        }

        liveData.observe(view, Observer { model ->

            if (!model.initted) {
                return@Observer
            }

            if (model.failed) { //failure

                if (model.nextUrl == null) {//first request
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
        return liveData.value ?: PokemonListDataModel()
    }

    fun <T> fetchData(
        context: Context?,
        view: T,
        subscribeOn: Scheduler? = Schedulers.io(), observeOn: Scheduler? = AndroidSchedulers.mainThread()
    ) where  T : MainActivityContract, T : LifecycleOwner {

        view.hideInitialLoadDataError()

        if (providePokemonListDataModel().nextUrl == null && providePokemonListDataModel().previous != null) {
            return
        }

        configObserver(view)
        configView(view)

        //getting pokemon list (each item contains just name and url)
        disposable = fetchResultPage(context, subscribeOn, observeOn)
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

    fun fetchResultPage(
        context: Context?,
        subscribeOn: Scheduler? = Schedulers.io(), observeOn: Scheduler? =  AndroidSchedulers.mainThread()
    ): Single<PokemonListDataModel> {

        val apiClient: ApiClient = networkModule.provideApiClient(context)

        val url = providePokemonListDataModel().nextUrl

        var resultPage: Single<ResultPage<BaseEntity>> = if (url == null)
            apiClient.fetchPokemons() else //first request
            apiClient.fetchPokemons(url) //next requests

        //getting pokemon list (each item contains just name and url)
        if (subscribeOn != null) {
            resultPage = resultPage.subscribeOn(Schedulers.io())
        }

        if (observeOn != null) {
            resultPage = resultPage.observeOn(observeOn)
        }

        //getting pokemon list (each item contains just name and url)
        var result = resultPage
            .flatMap { resultPage ->
                //getting data details for each pokemon
                fetchPokemonsForPage(context, resultPage, url, subscribeOn, observeOn)
            }


        //getting pokemon list (each item contains just name and url)
        if (subscribeOn != null) {
            result = result.subscribeOn(Schedulers.io())
        }

        if (observeOn != null) {
            result = result.observeOn(observeOn)
        }

        return result

    }

    fun fetchPokemon(
        context: Context?,
        baseEntity: BaseEntity,
        subscribeOn: Scheduler? = Schedulers.io(),
        observeOn: Scheduler? =  AndroidSchedulers.mainThread()
    ): Single<Pokemon> {
        var request = networkModule.provideApiClient(context).getPokemon(provideUrl(baseEntity))
            .map { pokemon ->
                pokemon.name = baseEntity.name
                pokemon.url = baseEntity.url
                pokemon
            }

        //getting pokemon list (each item contains just name and url)
        if (subscribeOn != null) {
            request = request.subscribeOn(Schedulers.io())
        }

        if (observeOn != null) {
            request = request.observeOn(observeOn)
        }

        return request
    }

    fun fetchPokemonsForPage(
        context: Context?,
        resultPage: ResultPage<BaseEntity>,
        url: String?,
        subscribeOn: Scheduler? = Schedulers.io(), observeOn: Scheduler? = AndroidSchedulers.mainThread()
    ): Single<PokemonListDataModel> {
        return Single.zip(resultPage.results.map { baseEntity ->
            fetchPokemon(context, baseEntity, subscribeOn, observeOn)
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

    open fun provideUrl(baseEntity: BaseEntity): String {
        return baseEntity.url
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun isProgressing(): Boolean {
        return !(disposable?.isDisposed ?: true)
    }

}