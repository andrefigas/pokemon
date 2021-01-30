package andrefigas.com.github.pokemon.view.details

import android.graphics.drawable.Drawable

interface DetailsActivityContract {

    fun showPokemonImage(drawable : Drawable)

    fun showPokemonName(name : String)

    fun showPokemonDescription(description : String)

    fun  showStartingDataProgress()

    fun  hideStartingDataProgress()

    fun showTypes(types: List<String>)

    fun showHabitat(habitat : String)

    fun showPokemonWeight(weight: Int)

    fun showPokemonHeight(height: Int)

    abstract fun showPokemonMoves(moves: List<String>)

}