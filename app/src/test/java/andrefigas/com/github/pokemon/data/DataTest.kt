package andrefigas.com.github.pokemon.data

import andrefigas.com.github.pokemon.MockApiClient
import andrefigas.com.github.pokemon.model.entities.BaseEntity
import andrefigas.com.github.pokemon.model.entities.Pokemon

object DataTest {

    val NULL: String = "null"

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

    val IVYSAUR_ENTITY: BaseEntity = BaseEntity(
        "ivysaur",
        "https://pokeapi.co/api/v2/pokemon/1/"
    )

    val IVYSAUR by lazy {
        val bulbasaur = Pokemon(
            "ivysaur",
            "https://pokeapi.co/api/v2/pokemon/1/"
        )
        bulbasaur.id = 2
        bulbasaur.weight = 130
        bulbasaur.height = 10
        bulbasaur
    }

    fun providePage(
        count: Int = Int.MAX_VALUE,
        offset: Int,
        limit: Int = 1,
        results: List<BaseEntity>
    ): String {
        val sb = StringBuilder()
        sb.append("{\n")
        sb.append("  \"count\": $count,\n")

        val next = offset + 1
        val nextStr =
            if (next >= count) NULL else "https://pokeapi.co/api/v2/pokemon?offset=$next&limit=$limit"

        sb.append(" \"next\": \"$nextStr\",\n")

        val previous = offset - 1
        val previousStr =
            if (previous < 0) NULL else "https://pokeapi.co/api/v2/pokemon?offset=$previous&limit=$limit"

        sb.append("  \"previous\": \"$previousStr\",\n")
        sb.append("  \"results\": [\n")

        results.forEach {
            sb.append("    {\n")
            sb.append("      \"name\": \"${it.name}\",\n")
            sb.append("      \"url\": \"${it.url}\"\n")
            sb.append("    }\n")
        }

        sb.append("  ]\n")
        sb.append("}")
        return sb.toString()
    }

    fun providePokemon(pokemon: Pokemon): String {
        val sb = StringBuilder()
        sb.append("{\n")
        sb.append("      \"name\": \"${pokemon.name}\",\n")
        sb.append("      \"weight\": \"${pokemon.weight}\",\n")
        sb.append("      \"height\": \"${pokemon.height}\",\n")
        sb.append("      \"id\": \"${pokemon.id}\"")
        sb.append("}\n")

        return sb.toString()
    }


}