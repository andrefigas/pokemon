package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.view.entities.PokemonListData
import andrefigas.com.github.pokemon.domain.usecases.GetPokemonsUseCase
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.*
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

    private val _initialLoad = MutableLiveData<PokemonListData.InitialSuccess>()
    val initialLoad : LiveData<PokemonListData.InitialSuccess> = _initialLoad

    private val _increaseLoad = MutableLiveData<PokemonListData.IncreaseSuccess>()
    val increaseLoad : LiveData<PokemonListData.IncreaseSuccess> = _increaseLoad

    private val _initialError = MutableLiveData<PokemonListData.InitialError>()
    val initialError : LiveData<PokemonListData.InitialError> = _initialError

    private val _increaseError = MutableLiveData<PokemonListData.IncreaseError>()
    val increaseError : LiveData<PokemonListData.IncreaseError> = _increaseError

    private val _image = MutableLiveData<PokemonListData.LoadImage>()
    val image : LiveData<PokemonListData.LoadImage> = _image

    fun fetchData(){

        disposable = getPokemonsUseCase.providePokemons()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->

                getPokemonsUseCase.injectUrl(response.nextUrl?:"")//todo fix

                if(response.previous.isNullOrEmpty()){
                    _initialLoad.value = PokemonListData.InitialSuccess(response.pokemons)
                }else{
                    _increaseLoad.value = PokemonListData.IncreaseSuccess(response.pokemons)
                }

        }, {

            if(getPokemonsUseCase.isInitialRequest()){
                _initialError.value = PokemonListData.InitialError
            }else{
                _increaseError.value = PokemonListData.IncreaseError
            }

        })
    }

    fun fetchImage(context : Context, pokemon: Pokemon){

        val imageLoader = ImageLoader.Builder(context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()

        val target = object : Target {
            override fun onSuccess(result: Drawable) {
                _image.value = PokemonListData.LoadImage(result, pokemon)
            }

            override fun onError(error: Drawable?) {
                if(error != null){
                    _image.value = PokemonListData.LoadImage(error, pokemon)
                }

            }

            override fun onStart(placeholder: Drawable?) {
                if(placeholder != null){
                    _image.value = PokemonListData.LoadImage(placeholder, pokemon)
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