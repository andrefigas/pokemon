package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.domain.usecases.GetPokemonDetailsUseCase
import andrefigas.com.github.pokemon.domain.usecases.GetPokemonImageUseCase
import andrefigas.com.github.pokemon.domain.usecases.UpdatePokemonFavoriteUseCase
import andrefigas.com.github.pokemon.intent.ImagePageState
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageEffect
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageEvent
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageState
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
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.schedulers.Schedulers

class PokemonDetailsViewModel (private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase,
            private val getPokemonImageUseCase: GetPokemonImageUseCase,
            private val updatePokemonFavouriteUseCase: UpdatePokemonFavoriteUseCase,
            val mapperContract: MapperContract) :
    ViewModel() {

    private val _pageState = MutableLiveData<PokemonDetailsPageState>()
    val pageState : LiveData<PokemonDetailsPageState> = _pageState

    private val _imageState = MutableLiveData<ImagePageState>()
    val imageState : LiveData<ImagePageState> = _imageState

    val effects: PublishSubject<PokemonDetailsPageEffect> = PublishSubject.create<PokemonDetailsPageEffect>()

    private var fetchDisposable: Disposable? = null
    private var updateDisposable: Disposable? = null
    private var imageDisposable: coil.request.Disposable? = null

    fun processEvent(event: PokemonDetailsPageEvent) {
        when(event){
            is PokemonDetailsPageEvent.OnCreate -> fetchData()
            is PokemonDetailsPageEvent.OnRequestImage -> fetchImage(event.context)
            PokemonDetailsPageEvent.OnAddToFavorites -> updateFavourite(true)
            PokemonDetailsPageEvent.OnRemoveFromFavorites -> updateFavourite(false)
        }
    }

    private fun fetchData(){

        if(_pageState.value != null){
            return
        }

        _pageState.value = PokemonDetailsPageState.Loading

        fetchDisposable = getPokemonDetailsUseCase.provideDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { details ->
                    _pageState.value = PokemonDetailsPageState.DetailsSuccess(details)
                },
                {
                    _pageState.value = PokemonDetailsPageState.DetailsFail
                }
            )

    }

    private fun fetchImage(context : Context){

        if(_imageState.value != null){
            return
        }

        val imageLoader = ImageLoader.Builder(context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()

        val target = object : Target {
            override fun onSuccess(result: Drawable) {
                _imageState.value = ImagePageState.OnSuccess(result)
            }

            override fun onError(error: Drawable?) {
                if(error != null){
                    _imageState.value = ImagePageState.OnFail(error)
                }

            }

            override fun onStart(placeholder: Drawable?) {
                if(placeholder != null){
                    _imageState.value = ImagePageState.OnStart(placeholder)
                }
            }
        }

        imageDisposable = getPokemonImageUseCase.loadPokemonImage(target).let { imageLoader.enqueue(it) }
    }

    private fun updateFavourite(isChecked : Boolean){

        if(isUpdateProgressing()) return

        val pageData = _pageState.value as PokemonDetailsPageState.DetailsSuccess

        updateDisposable = updatePokemonFavouriteUseCase.updateFavourite(isChecked)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { favoriteResponse ->

                    pageData.data.favoriteResponse.favorite = favoriteResponse.favorite

                    val name = pageData.data.pokemon.name

                    if(isChecked){
                        effects.onNext(PokemonDetailsPageEffect.OnAddToFavoriteSuccess(name))
                    }else{
                        effects.onNext(PokemonDetailsPageEffect.OnRemoveFromFavoriteSuccess(name))
                    }

                    _pageState.value = pageData
                },
                {
                    val name = pageData.data.pokemon.name
                    if(isChecked){
                        effects.onNext(PokemonDetailsPageEffect.OnAddToFavoriteFail(name))
                    }else{
                        effects.onNext(PokemonDetailsPageEffect.OnRemoveFromFavoriteFail(name))
                    }
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        fetchDisposable?.dispose()
        imageDisposable?.dispose()
        updateDisposable?.dispose()
    }

    private fun isUpdateProgressing(): Boolean {
        return !(updateDisposable?.isDisposed ?: true)
    }

}