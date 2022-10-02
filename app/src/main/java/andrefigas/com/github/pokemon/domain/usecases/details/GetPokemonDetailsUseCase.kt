package andrefigas.com.github.pokemon.domain.usecases.details

import andrefigas.com.github.pokemon.data.entities.FavoriteResponse
import andrefigas.com.github.pokemon.data.entities.PokemonDetailsDataModel
import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepositoryContract
import andrefigas.com.github.pokemon.domain.entities.Specie
import andrefigas.com.github.pokemon.domain.usecases.BaseUseCase
import andrefigas.com.github.pokemon.domain.usecases.BaseUseCaseContract
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.functions.Function

class GetPokemonDetailsUseCase(private val repo: PokemonDetailsRepositoryContract) : BaseUseCase(),
    GetPokemonDetailsUseCaseContract {

    //from Array<Any> to PokemonDetailsDataModel
    private val transformer : Function<Array<Any>, PokemonDetailsDataModel> =
        Function{ results ->
            var specie : Specie? = null
            var favorite : FavoriteResponse? = null

            results.forEach {  any ->
                when(any){
                    is Specie ->{
                        specie =  any
                    }

                    is FavoriteResponse ->{
                        favorite = any
                    }
                }
            }

            PokemonDetailsDataModel(repo.pokemon,
                specie as Specie,
                favorite as FavoriteResponse
            )
        }

    override operator fun invoke(onSuccess : Consumer<PokemonDetailsDataModel>, onFail : Consumer<Throwable>) {
        val request = listOf(
            repo.provideSpecie(),
            repo.provideFavourite()
        )

        disposable = Single.zip(
            request,
            transformer
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onFail)

    }

}

interface GetPokemonDetailsUseCaseContract : BaseUseCaseContract {

    operator fun invoke(onSuccess: Consumer<PokemonDetailsDataModel>, onFail: Consumer<Throwable>)

}


