package andrefigas.com.github.pokemon.intent.details

import android.content.Context

sealed class PokemonDetailsPageEvent {

    object OnCreate : PokemonDetailsPageEvent()

    class OnRequestImage(val context: Context) : PokemonDetailsPageEvent()

    object OnAddToFavorites : PokemonDetailsPageEvent()

    object OnRemoveFromFavorites : PokemonDetailsPageEvent()

}