package andrefigas.com.github.pokemon.view.entities

import android.graphics.drawable.Drawable

sealed class PokemonDetailsData {

    class DetailsSuccess(val data : PokemonDetailsUI) : PokemonDetailsData()

    class UpdateSuccess(val checked : Boolean) : PokemonDetailsData()

    class LoadImage(val data : Drawable) : PokemonDetailsData()

    object DetailsError : PokemonDetailsData()

    class UpdateError(val checked : Boolean) : PokemonDetailsData()


}