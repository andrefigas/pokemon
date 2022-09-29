package andrefigas.com.github.pokemon.domain.usecases

import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepositoryContract

class GetPokemonDetailsUseCase(private val repo: PokemonDetailsRepositoryContract) {

    fun provideDetails() = repo.provideDetails()

}
