package andrefigas.com.github.pokemon.domain.usecases.details

import andrefigas.com.github.pokemon.data.entities.UpdateFavoriteResponse
import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepositoryContract
import andrefigas.com.github.pokemon.domain.usecases.BaseUseCase
import andrefigas.com.github.pokemon.domain.usecases.BaseUseCaseContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class UpdatePokemonFavoriteUseCase(private val repo: PokemonDetailsRepositoryContract) : BaseUseCase(),
    UpdatePokemonFavoriteUseCaseContract {

    override operator fun invoke(favourite : Boolean, onSuccess : Consumer<UpdateFavoriteResponse>,
                        onFail : Consumer<Throwable>) = repo.updateFavourite(favourite)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, onFail)

}

interface UpdatePokemonFavoriteUseCaseContract : BaseUseCaseContract {

    operator fun invoke(
        favourite: Boolean,
        onSuccess: Consumer<UpdateFavoriteResponse>,
        onFail: Consumer<Throwable>
    ): Disposable

}
