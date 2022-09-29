package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.domain.usecases.GetPokemonsUseCase
import andrefigas.com.github.pokemon.intent.ImagePageState
import andrefigas.com.github.pokemon.intent.list.PokemonListPageEvent
import andrefigas.com.github.pokemon.intent.list.PokemonListPageState
import andrefigas.com.github.pokemon.view.entities.PokemonListData
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.target.Target
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


open class PokemonListViewModel (private val getPokemonsUseCase: GetPokemonsUseCase,
                                 val mapperContract: MapperContract) :
    ViewModel() {

    private var disposable: Disposable? = null

    private val _pageState = MutableLiveData<PokemonListPageState>()
    val pageState : LiveData<PokemonListPageState> = _pageState

    private val _imageState = MutableLiveData<ImagePageState>()
    val imageState : LiveData<ImagePageState> = _imageState

    fun processEvent(event : PokemonListPageEvent){
        when(event){
            is PokemonListPageEvent.OnCreate -> {
                if(pageState.value == null){
                    fetchData()
                }
            }

            is PokemonListPageEvent.OnScrollEnd,
            is PokemonListPageEvent.OnRetry->{
                fetchData()
            }

            is PokemonListPageEvent.OnLoadCard -> fetchImage(event)
        }
    }

    private fun fetchData(){

        if(getPokemonsUseCase.isInitialRequest()){
            _pageState.value = PokemonListPageState.InitialLoading
        }else{
            _pageState.value = PokemonListPageState.IncrementalLoading
        }

        disposable = getPokemonsUseCase.providePokemons()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->

                getPokemonsUseCase.injectUrl(response.nextUrl)

                if(response.previous.isNullOrEmpty()){
                    _pageState.value = PokemonListPageState.InitialSuccess(response.pokemons)
                }else{
                    _pageState.value = PokemonListPageState.IncrementalSuccess(response.pokemons)
                }

            }, {

                if(getPokemonsUseCase.isInitialRequest()){
                    _pageState.value = PokemonListPageState.InitialFail
                }else{
                    _pageState.value = PokemonListPageState.IncrementalFail
                }

            })
    }

    private fun fetchImage(event: PokemonListPageEvent.OnLoadCard){

        val context : Context = event.context
        val pokemon: Pokemon = event.pokemon

        val imageLoader = ImageLoader.Builder(context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()

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

        imageLoader.enqueue(getPokemonsUseCase.loadPokemonImage(pokemon, target))
    }


    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun isProgressing(): Boolean {
        return !(disposable?.isDisposed ?: true)
    }

}