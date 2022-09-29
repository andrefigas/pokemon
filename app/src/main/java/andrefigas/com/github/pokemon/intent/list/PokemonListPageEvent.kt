package andrefigas.com.github.pokemon.intent.list

import andrefigas.com.github.pokemon.domain.entities.Pokemon
import android.content.Context

sealed class PokemonListPageEvent {

    object OnCreate : PokemonListPageEvent()

    object OnRetry : PokemonListPageEvent()

    object OnScrollEnd: PokemonListPageEvent()

    class OnLoadCard(val context: Context, val pokemon : Pokemon) : PokemonListPageEvent()

}