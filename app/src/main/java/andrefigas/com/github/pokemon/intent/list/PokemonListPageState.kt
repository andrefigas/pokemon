package andrefigas.com.github.pokemon.intent.list

import andrefigas.com.github.pokemon.domain.entities.Pokemon


sealed class PokemonListPageState(val pokemons : MutableList<Pokemon>) {

    fun idle() = Idle

    object Idle : PokemonListPageState(mutableListOf())

    fun recycled() = Recycled(pokemons)

    class Recycled(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    fun initialLoading() = InitialLoading(pokemons)

    class InitialLoading(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    fun incrementalLoading() = IncrementalLoading(pokemons)

    class IncrementalLoading(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    fun initialFail() = InitialFail(pokemons)

    class InitialFail(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    fun incrementalFail() = IncrementalFail(pokemons)

    class IncrementalFail(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    fun initialSuccess(newPokemons : Array<Pokemon>): InitialSuccess {
        pokemons.addAll(newPokemons)
        return InitialSuccess(pokemons)
    }
    class InitialSuccess(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    fun incrementalSuccess(newPokemons : Array<Pokemon>): IncrementalSuccess {
        pokemons.addAll(newPokemons)
        return IncrementalSuccess(pokemons)
    }

    class IncrementalSuccess(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)
}