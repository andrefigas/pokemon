package andrefigas.com.github.pokemon.viewmodel

import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
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

    private val _image = MutableLiveData<PokemonDetailsData.LoadImage>()
    val image : LiveData<PokemonDetailsData.LoadImage> = _image

    private val _updateFavorite = MutableLiveData<PokemonDetailsData.UpdateSuccess>()
    val updateFavorite : LiveData<PokemonDetailsData.UpdateSuccess> = _updateFavorite

    private val _updateFavoriteError = MutableLiveData<PokemonDetailsData.UpdateError>()
    val updateFavoriteError : LiveData<PokemonDetailsData.UpdateError> = _updateFavoriteError

    private var fetchDisposable: Disposable? = null
    private var updateDisposable: Disposable? = null
    private var imageDisposable: coil.request.Disposable? = null

    fun fetchData(){

        if(details.value != null){
            return
        }

        fetchDisposable = getPokemonDetailsUseCase.provideDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { details ->
                    _details.value = PokemonDetailsData.DetailsSuccess(details)
                },
                {
                    _detailsError.value = PokemonDetailsData.DetailsError
                }
            )

    }

    fun fetchImage(context : Context){

        if(_image.value != null){
            return
        }

        val imageLoader = ImageLoader.Builder(context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()

        val target = object : Target {
            override fun onSuccess(result: Drawable) {
                _image.value = PokemonDetailsData.LoadImage(result)
            }

            override fun onError(error: Drawable?) {
                if(error != null){
                    _image.value = PokemonDetailsData.LoadImage(error)
                }

            }

            override fun onStart(placeholder: Drawable?) {
                if(placeholder != null){
                    _image.value = PokemonDetailsData.LoadImage(placeholder)
                }
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

    private fun isUpdateProgressing(): Boolean {
        return !(updateDisposable?.isDisposed ?: true)
    }

}