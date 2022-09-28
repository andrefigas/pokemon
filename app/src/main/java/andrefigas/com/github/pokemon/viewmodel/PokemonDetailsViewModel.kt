package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.data.mappers.MapperContract
import andrefigas.com.github.pokemon.view.entities.PokemonDetailsData
import andrefigas.com.github.pokemon.domain.usecases.GetPokemonDetailsUseCase
import andrefigas.com.github.pokemon.domain.usecases.GetPokemonImageUseCase
import andrefigas.com.github.pokemon.domain.usecases.UpdatePokemonFavoriteUseCase
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

class PokemonDetailsViewModel (private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase,
            private val getPokemonImageUseCase: GetPokemonImageUseCase,
            private val updatePokemonFavouriteUseCase: UpdatePokemonFavoriteUseCase,
            val mapperContract: MapperContract) :
    ViewModel() {


    private val _details = MutableLiveData<PokemonDetailsData.DetailsSuccess>()
    val details : LiveData<PokemonDetailsData.DetailsSuccess> = _details

    private val _detailsError = MutableLiveData<PokemonDetailsData.DetailsError>()
    val detailsError : LiveData<PokemonDetailsData.DetailsError> = _detailsError

    private val _image = MutableLiveData<PokemonDetailsData.ImageLoadSuccess>()
    val image : LiveData<PokemonDetailsData.ImageLoadSuccess> = _image

    private val _updateFavorite = MutableLiveData<PokemonDetailsData.UpdateSuccess>()
    val updateFavorite : LiveData<PokemonDetailsData.UpdateSuccess> = _updateFavorite

    private val _updateFavoriteError = MutableLiveData<PokemonDetailsData.UpdateError>()
    val updateFavoriteError : LiveData<PokemonDetailsData.UpdateError> = _updateFavoriteError

    var fetchDisposable: Disposable? = null
    var updateDisposable: Disposable? = null
    var imageDisposable: coil.request.Disposable? = null

    fun fetchData(){
        fetchDisposable = getPokemonDetailsUseCase.provideDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { details ->
                    _details.value = PokemonDetailsData.DetailsSuccess(mapperContract.fromDataToUI(details)) //todo fix
                },
                {
                    _detailsError.value = PokemonDetailsData.DetailsError
                }
            )

    }

    fun fetchImage(context : Context){

        val imageLoader = ImageLoader.Builder(context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()

        val target = object : Target {
            override fun onSuccess(result: Drawable) {
                _image.value = PokemonDetailsData.ImageLoadSuccess(result)
            }
        }

        imageDisposable = getPokemonImageUseCase.loadPokemonImage(target)?.let { imageLoader.enqueue(it) }
    }

    fun updateFavourite(isChecked : Boolean){

        if(isUpdateProgressing()) return

        updateDisposable = updatePokemonFavouriteUseCase.updateFavourite(isChecked)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { favorite ->
                    _updateFavorite.value = PokemonDetailsData.UpdateSuccess(favorite.favorite)
                },
                {
                    _updateFavoriteError.value = PokemonDetailsData.UpdateError(isChecked)
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        fetchDisposable?.dispose()
        imageDisposable?.dispose()
        updateDisposable?.dispose()
    }

    fun isUpdateProgressing(): Boolean {
        return !(updateDisposable?.isDisposed ?: true)
    }

}