package andrefigas.com.github.pokemon.domain.usecases.list

import andrefigas.com.github.pokemon.data.entities.PokemonListDataModel
import andrefigas.com.github.pokemon.data.entities.ResultPage
import andrefigas.com.github.pokemon.data.repository.PokemonRepositoryContract
import andrefigas.com.github.pokemon.domain.entities.BaseEntity
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.domain.usecases.BaseUseCase
import andrefigas.com.github.pokemon.domain.usecases.BaseUseCaseContract
import io.reactivex.Single
import io.reactivex.functions.Consumer

open class BasePokemonListUseCase(protected val repo: PokemonRepositoryContract) : BaseUseCase() {

    protected fun fetchPokemonsForPage(
        resultPage: ResultPage<BaseEntity>
    ): Single<PokemonListDataModel> {
        return Single.zip(resultPage.results.map { baseEntity ->
            repo.fetchPokemon(baseEntity)
        }) { pokemonPages ->
            val list = pokemonPages.map { pokemon ->
                pokemon as Pokemon
            }
            list.sortedBy { it.id }
            list.toTypedArray()
        }.map { pokemons ->
            PokemonListDataModel(
                pokemons,
                resultPage,
                resultPage.next
            )
        }

    }

}

interface BasePokemonListUseCaseContract : BaseUseCaseContract{

    operator fun invoke(onSuccess: Consumer<PokemonListDataModel>, onFail: Consumer<Throwable>)

}