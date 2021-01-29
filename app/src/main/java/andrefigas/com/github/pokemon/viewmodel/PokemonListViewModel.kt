package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.model.entities.Pokemon
import andrefigas.com.github.pokemon.injection.modules.NetworkModule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PokemonListViewModel @Inject constructor(private val networkModule: NetworkModule) :
    ViewModel() {
    private val compositeDisposable =
        CompositeDisposable()
    private val livedata =
        MutableLiveData<MutableList<Pokemon>>()

    private lateinit var nextUrl : String

    fun init(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<List<Pokemon>>?
    ) {
        livedata.observe(lifecycleOwner!!, observer!!)
        val limit = "10"
        val offset = "0"

        compositeDisposable.add(
            networkModule.provideApiClient().fetchPokemons(limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    nextUrl = it.next

                    val requests =  it.results.map { baseEntity->
                        networkModule.provideApiClient().getPokemon(baseEntity.url).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).map { pokemon ->
                                Pokemon(baseEntity.name, baseEntity.url, pokemon.weight, pokemon.height)
                            }
                    }

                    Single.zip(requests, { pokemonPages ->
                        pokemonPages.map { it as Pokemon }
                    })
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    livedata.value = it.toMutableList()
                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}