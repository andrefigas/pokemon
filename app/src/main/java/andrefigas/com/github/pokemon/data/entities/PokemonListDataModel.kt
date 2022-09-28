package andrefigas.com.github.pokemon.data.entities

import andrefigas.com.github.pokemon.domain.entities.BaseEntity
import andrefigas.com.github.pokemon.domain.entities.Pokemon

class PokemonListDataModel() {

    var initted: Boolean = false
    var failed: Boolean = false
    var nextUrl: String? = null
    var previous: String? = null
    var pokemons: List<Pokemon> = mutableListOf()
    var currentUrl: String? = null

    constructor(
        pokemons: List<Pokemon>,
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
}
