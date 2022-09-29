package andrefigas.com.github.pokemon.data.entities

import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.domain.entities.Specie

class PokemonDetailsDataModel(val pokemon: Pokemon) {
    var species: Specie? = null
    var favoriteResponse: FavoriteResponse? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PokemonDetailsDataModel

        if (pokemon != other.pokemon) return false
        if (species != other.species) return false
        if (favoriteResponse != other.favoriteResponse) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pokemon.hashCode()
        result = 31 * result + (species?.hashCode() ?: 0)
        result = 31 * result + (favoriteResponse?.hashCode() ?: 0)
        return result
    }


}