package andrefigas.com.github.pokemon.domain.usecases

import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepositoryContract
import coil.target.Target

class GetPokemonImageUseCase(private val repo: PokemonDetailsRepositoryContract) {

    fun loadPokemonImage(target: Target) = repo.loadPokemonImage(target)

}
