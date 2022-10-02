package andrefigas.com.github.pokemon.domain.usecases.list

import andrefigas.com.github.pokemon.data.entities.PokemonListDataModel
import andrefigas.com.github.pokemon.data.repository.PokemonRepositoryContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class GetInitialPokemonsPageUseCase(repo: PokemonRepositoryContract) : BasePokemonListUseCase(repo),
    GetchInitialPokemonsPageUseCaseContract {


    override operator fun invoke(onSuccess: Consumer<PokemonListDataModel>, onFail: Consumer<Throwable>) {
        disposable = repo.fetchInitialPokemonsPage().flatMap { resultPage ->
            fetchPokemonsForPage(resultPage)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onFail)
    }


}

interface GetchInitialPokemonsPageUseCaseContract : BasePokemonListUseCaseContract
