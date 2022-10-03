package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.domain.usecases.details.GetPokemonDetailsUseCaseContract
import andrefigas.com.github.pokemon.domain.usecases.details.GetPokemonDetailsImageUseCaseContract
import andrefigas.com.github.pokemon.domain.usecases.details.UpdatePokemonFavoriteUseCaseContract
import andrefigas.com.github.pokemon.intent.ImagePageState
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageEffect
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageEvent
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageState
import andrefigas.com.github.pokemon.utils.changeState
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.target.Target
import io.reactivex.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject

class PokemonDetailsViewModel(
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCaseContract,
    private val getPokemonImageUseCase: GetPokemonDetailsImageUseCaseContract,
    private val updatePokemonFavouriteUseCase: UpdatePokemonFavoriteUseCaseContract,
    val mapperContract: MapperContract
) :
    ViewModel() {

    private val _pageState : MutableLiveData<PokemonDetailsPageState>
    val pageState: LiveData<PokemonDetailsPageState>

    private val _imageState : MutableLiveData<ImagePageState>
    val imageState: LiveData<ImagePageState>

    val effects: PublishSubject<PokemonDetailsPageEffect>

    init {
        _pageState = MutableLiveData(PokemonDetailsPageState.Idle)
        pageState = _pageState

        _imageState = MutableLiveData(ImagePageState.Idle)
        imageState = _imageState

       effects = PublishSubject.create<PokemonDetailsPageEffect>()
    }

    fun processEvent(event: PokemonDetailsPageEvent) {
        when (event) {
            is PokemonDetailsPageEvent.OnCreate -> fetchData()
            is PokemonDetailsPageEvent.OnRequestImage -> fetchImage(event.context)
            PokemonDetailsPageEvent.OnAddToFavorites -> updateFavourite(true)
            PokemonDetailsPageEvent.OnRemoveFromFavorites -> updateFavourite(false)
        }
    }

    private fun fetchData() {

        if (_pageState.value !is PokemonDetailsPageState.Idle) {
            return
        }

        _pageState.changeState {
            it.loading()
        }

        getPokemonDetailsUseCase(
            Consumer { details ->
                _pageState.changeState {
                    it.detailsSuccess(details)
                }
            },
            Consumer {
                _pageState.changeState {
                    it.detailsFail()
                }
            }
        )

    }

    private fun fetchImage(context: Context) {

        if (_imageState.value != ImagePageState.Idle) {
            return
        }

        getPokemonImageUseCase(context, object : Target {
            override fun onSuccess(result: Drawable) {
                _imageState.changeState {
                    it.success(result)
                }
            }

            override fun onError(error: Drawable?) {
                _imageState.changeState {
                    it.fail(error)
                }

            }

            override fun onStart(placeholder: Drawable?) {
                _imageState.changeState {
                    it.fail(placeholder)
                }
            }
        })

    }

    private fun updateFavourite(isChecked: Boolean) {

        if (isUpdateProgressing()) return

        _pageState.changeState {
            it.updateFavoriteInProgress()
        }

        val name = pageState.value?.data?.pokemon?.name?:return

        updatePokemonFavouriteUseCase(isChecked,
            Consumer { favoriteResponse ->

                if (favoriteResponse.favorite) {
                    effects.onNext(PokemonDetailsPageEffect.OnAddToFavoriteSuccess(name))
                } else {
                    effects.onNext(PokemonDetailsPageEffect.OnRemoveFromFavoriteSuccess(name))
                }

                _pageState.changeState {
                    it.updateFavoriteInSuccess(favoriteResponse)
                }
            },
            Consumer {
                if (isChecked) {
                    effects.onNext(PokemonDetailsPageEffect.OnAddToFavoriteFail(name))
                } else {
                    effects.onNext(PokemonDetailsPageEffect.OnRemoveFromFavoriteFail(name))
                }
            })
    }

    override fun onCleared() {
        super.onCleared()
        release()
    }

    fun release(){
        releaseUseCases()
        recycleImageState()
        recyclePageState()
    }

    private fun releaseUseCases(){
        getPokemonDetailsUseCase.dispose()
        getPokemonImageUseCase.dispose()
        updatePokemonFavouriteUseCase.dispose()
    }

    private fun recyclePageState(){
        when(pageState.value){
            is PokemonDetailsPageState.DetailsFail,
            is PokemonDetailsPageState.Loading ->{
                _pageState.changeState {
                    it.idle()
                }
            }
            else -> {
                _pageState.changeState {
                    it.recycled()
                }
            }
        }
    }

    private fun recycleImageState(){
        if(imageState.value is ImagePageState.OnSuccess){
            _imageState.changeState {
                it.recycle()
            }
        } else {
            _imageState.changeState {
                it.idle()
            }
        }
    }

    private fun isUpdateProgressing(): Boolean {
        return _pageState.value is PokemonDetailsPageState.UpdateFavoriteInProgress
    }

}