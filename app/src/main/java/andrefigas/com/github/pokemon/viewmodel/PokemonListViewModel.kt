package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.domain.usecases.list.GetNextPokemonsPageUseCaseContract
import andrefigas.com.github.pokemon.domain.usecases.list.GetPokemonListImageUseCaseContract
import andrefigas.com.github.pokemon.domain.usecases.list.GetchInitialPokemonsPageUseCaseContract
import andrefigas.com.github.pokemon.intent.ImagePageState
import andrefigas.com.github.pokemon.intent.list.PokemonListPageEvent
import andrefigas.com.github.pokemon.intent.list.PokemonListPageState
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.target.Target
import io.reactivex.functions.Consumer


open class PokemonListViewModel (private val getInitialPokemonsPageUseCase: GetchInitialPokemonsPageUseCaseContract,
                                 private val getNextPokemonsPageUseCase: GetNextPokemonsPageUseCaseContract,
                                 private val getPokemonsImageUseCaseContract: GetPokemonListImageUseCaseContract,
                                 val mapperContract: MapperContract) :
    ViewModel() {

    private val _pageState = MutableLiveData<PokemonListPageState>()
    val pageState : LiveData<PokemonListPageState> = _pageState

    private val _imageState = MutableLiveData<ImagePageState>()
    val imageState : LiveData<ImagePageState> = _imageState

    fun processEvent(event : PokemonListPageEvent){
        when(event){
            is PokemonListPageEvent.OnCreate -> {
                if(pageState.value == null){
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
        _pageState.value = PokemonListPageState.InitialLoading

        getInitialPokemonsPageUseCase.invoke(Consumer { response ->
            _pageState.value = PokemonListPageState.InitialSuccess(response.pokemons)
        }, Consumer {
            _pageState.value = PokemonListPageState.InitialFail
        })
    }

    private fun fetchNextPage(){
        _pageState.value = PokemonListPageState.IncrementalLoading

        getNextPokemonsPageUseCase(Consumer { response ->
            _pageState.value = PokemonListPageState.IncrementalSuccess(response.pokemons)
        }, Consumer {
            _pageState.value = PokemonListPageState.IncrementalFail
        })
    }

    private fun fetchImage(event: PokemonListPageEvent.OnLoadCard){

        val context : Context = event.context
        val pokemon: Pokemon = event.pokemon

        val target = object : Target {
            override fun onSuccess(result: Drawable) {
                _imageState.value = ImagePageState.OnSuccess(result, pokemon.url)
            }

            override fun onError(error: Drawable?) {
                if(error != null){
                    _imageState.value = ImagePageState.OnFail(error, pokemon.url)
                }

            }

            override fun onStart(placeholder: Drawable?) {
                if(placeholder != null){
                    _imageState.value = ImagePageState.OnStart(placeholder, pokemon.url)
                }
            }
        }

        getPokemonsImageUseCaseContract(context, pokemon, target)
    }


    override fun onCleared() {
        super.onCleared()
        getInitialPokemonsPageUseCase.dispose()
    }

    fun isLoading()  = when(pageState.value){
        is PokemonListPageState.InitialLoading,
        is PokemonListPageState.IncrementalLoading -> true
        else -> false
    }

}