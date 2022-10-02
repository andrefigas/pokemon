package andrefigas.com.github.pokemon.data.entities

import andrefigas.com.github.pokemon.domain.entities.BaseEntity
import andrefigas.com.github.pokemon.domain.entities.Pokemon

class PokemonListDataModel() {

    private var initted: Boolean = false
    private var failed: Boolean = false
    private var nextUrl: String? = null
    private var previous: String? = null
    var pokemons: Array<Pokemon> = emptyArray()
    private var currentUrl: String? = null

    constructor(
        pokemons: Array<Pokemon>,
        page: ResultPage<BaseEntity>,
        currentUrl: String?
    ) : this() {
        initted = true
        failed = false
        nextUrl = page.next
        previous = page.previous
        this.pokemons = pokemons
        this.currentUrl = currentUrl
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PokemonListDataModel

        if (!pokemons.contentEquals(other.pokemons)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = 0

        result = 31 * result + pokemons.hashCode()

        return result
    }


}
