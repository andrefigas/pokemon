package andrefigas.com.github.pokemon.data.entities

import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.domain.entities.Specie

class PokemonDetailsDataModel(val pokemon: Pokemon) {
    var species: Specie? = null
    var favoriteResponse: FavoriteResponse? = null
}