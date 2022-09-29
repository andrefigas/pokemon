package andrefigas.com.github.pokemon.intent.list

sealed class PokemonListPageEvent {

    object OnCreate : PokemonListPageEvent()

    object OnRetry : PokemonListPageEvent()

    object OnScrollEnd: PokemonListPageEvent()

}