package andrefigas.com.github.pokemon.domain.usecases

import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepositoryContract


class UpdatePokemonFavoriteUseCase(private val repo: PokemonDetailsRepositoryContract) {

    fun updateFavourite(favourite : Boolean) = repo.updateFavourite(favourite)

}
