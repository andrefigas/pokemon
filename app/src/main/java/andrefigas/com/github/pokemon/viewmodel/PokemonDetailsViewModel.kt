package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.domain.usecases.details.GetPokemonDetailsUseCaseContract
import andrefigas.com.github.pokemon.domain.usecases.details.GetPokemonDetailsImageUseCaseContract
import andrefigas.com.github.pokemon.domain.usecases.details.UpdatePokemonFavoriteUseCaseContract
import andrefigas.com.github.pokemon.intent.ImagePageState
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageEffect
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageEvent
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageState
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

    private val _pageState = MutableLiveData<PokemonDetailsPageState>()
    val pageState: LiveData<PokemonDetailsPageState> = _pageState

    private val _imageState = MutableLiveData<ImagePageState>()
    val imageState: LiveData<ImagePageState> = _imageState

    val effects: PublishSubject<PokemonDetailsPageEffect> =
        PublishSubject.create<PokemonDetailsPageEffect>()

    fun processEvent(event: PokemonDetailsPageEvent) {
        when (event) {
            is PokemonDetailsPageEvent.OnCreate -> fetchData()
            is PokemonDetailsPageEvent.OnRequestImage -> fetchImage(event.context)
            PokemonDetailsPageEvent.OnAddToFavorites -> updateFavourite(true)
            PokemonDetailsPageEvent.OnRemoveFromFavorites -> updateFavourite(false)
        }
    }

    private fun fetchData() {

        if (_pageState.value != null) {
            return
        }

        _pageState.value = PokemonDetailsPageState.Loading

        getPokemonDetailsUseCase(
            Consumer { details ->
                _pageState.value = PokemonDetailsPageState.DetailsSuccess(details)
            },
            Consumer {
                _pageState.value = PokemonDetailsPageState.DetailsFail
            }
        )

    }

    private fun fetchImage(context: Context) {

        if (_imageState.value != null) {
            return
        }

        getPokemonImageUseCase(context, object : Target {
            override fun onSuccess(result: Drawable) {
                _imageState.value = ImagePageState.OnSuccess(result)
            }

            override fun onError(error: Drawable?) {
                if (error != null) {
                    _imageState.value = ImagePageState.OnFail(error)
                }

            }

            override fun onStart(placeholder: Drawable?) {
                //do nothing
            }
        })

    }

    private fun updateFavourite(isChecked: Boolean) {

        if (isUpdateProgressing()) return

        val pageData = _pageState.value as PokemonDetailsPageState.DetailsSuccess

        _pageState.value = PokemonDetailsPageState.UpdateFavoriteInProgress

        updatePokemonFavouriteUseCase(isChecked,
            Consumer { favoriteResponse ->

                pageData.data.favoriteResponse.favorite = favoriteResponse.favorite

                val name = pageData.data.pokemon.name

                if (favoriteResponse.favorite) {
                    effects.onNext(PokemonDetailsPageEffect.OnAddToFavoriteSuccess(name))
                } else {
                    effects.onNext(PokemonDetailsPageEffect.OnRemoveFromFavoriteSuccess(name))
                }

                _pageState.value = pageData
            },
            Consumer {
                val name = pageData.data.pokemon.name
                if (isChecked) {
                    effects.onNext(PokemonDetailsPageEffect.OnAddToFavoriteFail(name))
                } else {
                    effects.onNext(PokemonDetailsPageEffect.OnRemoveFromFavoriteFail(name))
                }
            })
    }

    override fun onCleared() {
        super.onCleared()
        getPokemonDetailsUseCase.dispose()
        getPokemonImageUseCase.dispose()
        updatePokemonFavouriteUseCase.dispose()
    }

    private fun isUpdateProgressing(): Boolean {
        return _pageState.value == PokemonDetailsPageState.UpdateFavoriteInProgress
    }

}