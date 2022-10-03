package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.domain.usecases.list.GetNextPokemonsPageUseCaseContract
import andrefigas.com.github.pokemon.domain.usecases.list.GetPokemonListImageUseCaseContract
import andrefigas.com.github.pokemon.domain.usecases.list.GetchInitialPokemonsPageUseCaseContract
import andrefigas.com.github.pokemon.intent.ImagePageState
import andrefigas.com.github.pokemon.intent.list.PokemonListPageEvent
import andrefigas.com.github.pokemon.intent.list.PokemonListPageState
import andrefigas.com.github.pokemon.utils.changeState
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.target.Target
import io.reactivex.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject


open class PokemonListViewModel (private val getInitialPokemonsPageUseCase: GetchInitialPokemonsPageUseCaseContract,
                                 private val getNextPokemonsPageUseCase: GetNextPokemonsPageUseCaseContract,
                                 private val getPokemonsImageUseCaseContract: GetPokemonListImageUseCaseContract,
                                 val mapperContract: MapperContract) :
    ViewModel() {

    private val _pageState : MutableLiveData<PokemonListPageState>
    val pageState : LiveData<PokemonListPageState>
    val imageState : PublishSubject<Pair<String, Drawable?>>

    init {
        _pageState = MutableLiveData(PokemonListPageState.Idle)
        pageState  = _pageState
        imageState = PublishSubject.create()
    }

    fun processEvent(event : PokemonListPageEvent){
        when(event){
            is PokemonListPageEvent.OnCreate -> {
                if(pageState.value == PokemonListPageState.Idle){
                    fetchInitialPage()
                }
            }

            is PokemonListPageEvent.OnScrollEnd,
            is PokemonListPageEvent.OnRetry->{
                fetchNextPage()
            }

            is PokemonListPageEvent.OnLoadCard -> fetchImage(event)
        }
    }

    private fun fetchInitialPage(){
        _pageState.changeState {
            it.initialLoading()
        }

        getInitialPokemonsPageUseCase.invoke(Consumer { response ->
            _pageState.changeState {
                it.initialSuccess(response.pokemons)
            }


        }, Consumer {
            _pageState.changeState {
                it.initialFail()
            }
        })
    }

    private fun fetchNextPage(){

        _pageState.value = _pageState.value?.incrementalLoading()

        getNextPokemonsPageUseCase(Consumer { response ->
            _pageState.changeState {
                it.incrementalSuccess(response.pokemons)
            }
        }, Consumer {
            _pageState.changeState {
                it.incrementalFail()
            }
        })
    }

    private fun fetchImage(event: PokemonListPageEvent.OnLoadCard){

        val context : Context = event.context
        val pokemon: Pokemon = event.pokemon
        val url = pokemon.url

        val target = object : Target {
            override fun onSuccess(result: Drawable) {
                imageState.onNext(Pair(url, result))
            }

            override fun onError(error: Drawable?) {
                imageState.onNext(Pair(url, error))
            }

            override fun onStart(placeholder: Drawable?) {
                imageState.onNext(Pair(url, placeholder))
            }
        }

        getPokemonsImageUseCaseContract(context, pokemon, target)
    }


    override fun onCleared() {
        super.onCleared()
        release()
    }

    fun release(){
       releaseUseCases()
       recyclePageState()
    }

    private fun releaseUseCases(){
        getInitialPokemonsPageUseCase.dispose()
        getNextPokemonsPageUseCase.dispose()
        getPokemonsImageUseCaseContract.dispose()
    }

    private fun recyclePageState(){
        _pageState.changeState {
            when(it){
                is PokemonListPageState.InitialLoading,
                is PokemonListPageState.InitialFail ->{
                    it.idle()
                }
                else->{
                    it.recycled()
                }
            }
        }
    }

    fun isLoading()  = when(pageState.value){
        is PokemonListPageState.InitialLoading,
        is PokemonListPageState.IncrementalLoading -> true
        else -> false
    }

}