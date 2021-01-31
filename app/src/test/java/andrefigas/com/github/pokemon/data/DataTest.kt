package andrefigas.com.github.pokemon.data

import andrefigas.com.github.pokemon.model.entities.BaseEntity
import andrefigas.com.github.pokemon.model.entities.Pokemon

object DataTest {

    val BULBASAUR_ENTITY: BaseEntity = BaseEntity(
        "bulbasaur",
        "https://pokeapi.co/api/v2/pokemon/1/"
    )

    val BULBASAUR by lazy {
        val bulbasaur = Pokemon(
            "bulbasaur",
            "https://pokeapi.co/api/v2/pokemon/1/"
        )
        bulbasaur.id = 1
        bulbasaur.weight = 69
        bulbasaur.height = 7
        bulbasaur
    }


}