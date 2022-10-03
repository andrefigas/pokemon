package andrefigas.com.github.pokemon.intent.list

import andrefigas.com.github.pokemon.domain.entities.Pokemon


sealed class PokemonListPageState(val pokemons : MutableList<Pokemon>) {

    object Idle : PokemonListPageState(mutableListOf())

    class Recycled(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    class InitialLoading(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    class IncrementalLoading(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    class InitialFail(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    class IncrementalFail(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    class InitialSuccess(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    class IncrementalSuccess(pokemons : MutableList<Pokemon>) : PokemonListPageState(pokemons)

    //mappers

    fun incrementalSuccess(newPokemons : Array<Pokemon>): IncrementalSuccess {
        pokemons.addAll(newPokemons)
        return IncrementalSuccess(pokemons)
    }

    fun initialSuccess(newPokemons : Array<Pokemon>): InitialSuccess {
        pokemons.addAll(newPokemons)
        return InitialSuccess(pokemons)
    }

    fun incrementalFail() = IncrementalFail(pokemons)

    fun initialFail() = InitialFail(pokemons)

    fun recycled() = Recycled(pokemons)

    fun incrementalLoading() = IncrementalLoading(pokemons)

    fun initialLoading() = InitialLoading(pokemons)

    fun idle() = Idle

}