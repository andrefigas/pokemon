package andrefigas.com.github.pokemon.data.mappers

import andrefigas.com.github.pokemon.data.entities.FavoriteResponse
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.data.entities.PokemonDetailsDataModel
import andrefigas.com.github.pokemon.domain.entities.FavoritePokemon
import andrefigas.com.github.pokemon.domain.entities.Specie
import andrefigas.com.github.pokemon.view.entities.PokemonDetailsUI
import andrefigas.com.github.pokemon.view.entities.PokemonUI
import android.content.Context

class Mapper(val context: Context) : MapperContract {

    override fun fromDataToUI(data : PokemonDetailsDataModel) : PokemonDetailsUI {
        val pokemon: Pokemon = data.pokemon
        val specie: Specie = data.species as Specie
        val favoriteResponse : FavoriteResponse = data.favoriteResponse as FavoriteResponse

        val name = pokemon.name.capitalize()
        val description = specie.labels.firstOrNull { it.language?.name == "en" }?.label?:"".replace("\n", " ", false)
        val height = pokemon.height
        val weight = pokemon.weight
        val photo = pokemon.spritesCollection?.getBetterImage()?:""//todo fix
        val habitat = specie.habitat?.name?:""//todo fix
        val types = pokemon.types?.map {
            it.content?.name?:""
        }?: emptyList()
        val moves = pokemon.moves?.map {
            it.content?.name?:""
        }?: emptyList()

        val favourite = favoriteResponse.favorite

        return PokemonDetailsUI(
            name,
            description,
            height,
            weight,
            photo,
            habitat,
            types,
            moves,
            favourite
        )
    }

    override fun fromDataToUI(pokemon : Pokemon) : PokemonUI {
        val name = pokemon.name.capitalize()
        val height = pokemon.height
        val weight = pokemon.weight
        val photo = pokemon.spritesCollection?.getBetterImage()?:""//todo fix
        val types = pokemon.types?.map {
            it.content?.name?:""
        }?: emptyList()
        val moves = pokemon.moves?.map {
            it.content?.name?:""
        }?: emptyList()

        return PokemonUI(name, weight, height, photo,types, moves)
    }

    override fun fromUIToData(id: Int, check : Boolean) = FavoritePokemon(id, check)

}

interface  MapperContract{
    fun fromDataToUI(pokemonDetailsDataModel: PokemonDetailsDataModel): PokemonDetailsUI
    fun fromDataToUI(data: Pokemon): PokemonUI
    fun fromUIToData(id: Int, check: Boolean): FavoritePokemon
}