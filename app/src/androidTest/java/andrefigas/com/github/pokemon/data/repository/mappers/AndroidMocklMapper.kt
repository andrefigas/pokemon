package andrefigas.com.github.pokemon.data.repository.mappers

import andrefigas.com.github.pokemon.data.entities.PokemonDetailsDataModel
import andrefigas.com.github.pokemon.domain.entities.FavoritePokemon
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.view.entities.PokemonDetailsUI
import andrefigas.com.github.pokemon.view.entities.PokemonUI

val MockMapper = object :  MapperContract{
    override fun fromDataToUI(pokemonDetailsDataModel: PokemonDetailsDataModel): PokemonDetailsUI {
        throw NotImplementedError()
    }

    override fun fromDataToUI(data: Pokemon): PokemonUI {
        throw NotImplementedError()
    }

    override fun fromUIToData(id: Int, check : Boolean) = FavoritePokemon(id, check)

}