package andrefigas.com.github.pokemon.data

import andrefigas.com.github.pokemon.data.entities.FavoriteResponse
import andrefigas.com.github.pokemon.data.entities.PokemonDetailsDataModel
import andrefigas.com.github.pokemon.domain.entities.*

object DataTest {

    val BULBASAUR  = Pokemon(
        "bulbasaur",
        "https://pokeapi.co/api/v2/pokemon/1/"
    ).apply{

        id = 1
        weight = 69
        height = 7

        spritesCollection = SpritesCollection(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/1.png",
            null, null, null, null, null, null, null, null
        )

        species = BaseEntity("bulbasaur", "https://pokeapi.co/api/v2/pokemon-species/1/")
        types = arrayOf(
            Type(1, BaseEntity("grass", "https://pokeapi.co/api/v2/type/12/")),
            Type(2, BaseEntity("poison", "https://pokeapi.co/api/v2/type/4/"))
        )

        moves = arrayOf(
            Move(BaseEntity("razor-wind","https://pokeapi.co/api/v2/move/13/" ))
        )

    }

    val BULBASAUR_DETAILS = PokemonDetailsDataModel(BULBASAUR).apply {
        species = Specie(
            arrayOf(FlavorText().apply {
                label = "Bulbasaur can be seen napping in bright sunlight.\n" +
                        "There is a seed on its back. By soaking up the sun’s rays,\n" +
                        "the seed grows progressively larger."
                language =  BaseEntity("en", "https://pokeapi.co/api/v2/language/9/")
            }),
            BaseEntity("grassland", "https://pokeapi.co/api/v2/pokemon-habitat/3/")
        )

        favoriteResponse = FavoriteResponse(false)

    }

}