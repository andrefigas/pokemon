package andrefigas.com.github.pokemon.data.repository.mappers

import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.data.entities.FavoriteResponse
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.data.entities.PokemonDetailsDataModel
import andrefigas.com.github.pokemon.data.entities.FavoritePokemon
import andrefigas.com.github.pokemon.domain.entities.Specie
import andrefigas.com.github.pokemon.utils.toString
import andrefigas.com.github.pokemon.view.entities.PokemonDetailsUI
import andrefigas.com.github.pokemon.view.entities.PokemonUI
import android.content.Context

class Mapper(val context: Context) : MapperContract {

    override fun fromDataToUI(data : PokemonDetailsDataModel) : PokemonDetailsUI {
        val pokemon: Pokemon = data.pokemon
        val specie: Specie = data.species
        val favoriteResponse : FavoriteResponse = data.favoriteResponse

        val name = pokemon.name.capitalize()
        val description = specie.labels.firstOrNull { it.language.name == "en" }?.label?:"".replace("\n", " ", false)
        val height = pokemon.height
        val weight = pokemon.weight
        val habitat = specie.habitat.name
        val types = pokemon.types.map {
            it.content.name
        }
        val moves : List<String> = pokemon.moves.map {
            it.content.name
        }

        val favourite = favoriteResponse.favorite

        return PokemonDetailsUI(
            name,
            height,
            weight,
            types.toString(context.getString(R.string.types_separator)),
            moves.toString(context.getString(R.string.types_separator)),
            description,
            habitat,
            favourite
        )
    }

    override fun fromDataToUI(pokemon : Pokemon) : PokemonUI {
        val name = pokemon.name.capitalize()
        val height = pokemon.height
        val weight = pokemon.weight

        val types = pokemon.types.map {
            it.content.name
        }

        val moves :List<String> = pokemon.moves.map {
            it.content.name
        }

        return PokemonUI(name, weight, height,types.toString(context.getString(R.string.types_separator)), moves.toString(context.getString(R.string.types_separator)))
    }

    override fun fromUIToData(id: Int, check : Boolean) = FavoritePokemon(id, check)

}

interface  MapperContract{
    fun fromDataToUI(pokemonDetailsDataModel: PokemonDetailsDataModel): PokemonDetailsUI
    fun fromDataToUI(data: Pokemon): PokemonUI
    fun fromUIToData(id: Int, check: Boolean): FavoritePokemon
}