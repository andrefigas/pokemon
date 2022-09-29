package andrefigas.com.github.pokemon.intent.list

import andrefigas.com.github.pokemon.domain.entities.Pokemon


sealed class PokemonListPageState {

    object InitialLoading : PokemonListPageState()

    object IncrementalLoading : PokemonListPageState()

    object InitialFail : PokemonListPageState()

    object IncrementalFail : PokemonListPageState()

    class InitialSuccess(val pokemons : Array<Pokemon>) : PokemonListPageState()

    class IncrementalSuccess(val pokemons : Array<Pokemon>) : PokemonListPageState()
}