package andrefigas.com.github.pokemon.domain.usecases.list

import andrefigas.com.github.pokemon.data.entities.PokemonListDataModel
import andrefigas.com.github.pokemon.data.repository.PokemonRepositoryContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class GetNextPokemonsPageUseCase(repo: PokemonRepositoryContract) : BasePokemonListUseCase(repo),
    GetNextPokemonsPageUseCaseContract {


    override operator fun invoke(onSuccess: Consumer<PokemonListDataModel>, onFail: Consumer<Throwable>) {
        disposable = repo.fetchNextPokemonsPage().flatMap { resultPage ->
            fetchPokemonsForPage(resultPage)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onFail)
    }


}

interface GetNextPokemonsPageUseCaseContract : BasePokemonListUseCaseContract
