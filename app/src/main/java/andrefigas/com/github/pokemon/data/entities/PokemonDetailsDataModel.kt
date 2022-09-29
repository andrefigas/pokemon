package andrefigas.com.github.pokemon.data.entities

import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.domain.entities.Specie

data class PokemonDetailsDataModel(val pokemon: Pokemon,
                              val species: Specie,
                              val favoriteResponse: FavoriteResponse)