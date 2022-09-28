package andrefigas.com.github.pokemon.domain.usecases

import andrefigas.com.github.pokemon.data.repository.PokemonRepositoryContract
import andrefigas.com.github.pokemon.data.entities.PokemonListDataModel
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import coil.request.ImageRequest
import coil.target.Target
import io.reactivex.Single

class GetPokemonsUseCase(val repo: PokemonRepositoryContract) {

    fun providePokemons() : Single<PokemonListDataModel> = repo.providePokemons()

    fun isInitialRequest() = repo.isInitialRequest()

    fun injectUrl(url : String) = repo.injectUrl(url)

    fun loadPokemonImage(pokemon: Pokemon, target: Target) = repo.loadPokemonImage(pokemon, target)

}
