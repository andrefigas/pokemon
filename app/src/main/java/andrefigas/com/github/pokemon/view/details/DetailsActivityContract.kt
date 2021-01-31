package andrefigas.com.github.pokemon.view.details

import android.graphics.drawable.Drawable

interface DetailsActivityContract {

    fun showPokemonImage(drawable: Drawable)

    fun showPokemonErrorImage(drawable: Drawable)

    fun showPokemonName(name: String)

    fun showPokemonDescription(description: String)

    fun showStartingDataProgress()

    fun hideStartingDataProgress()

    fun showTypes(types: List<String>)

    fun showHabitat(habitat: String)

    fun showFavoriteChecked()

    fun showFavoriteUnchecked()

    fun showPokemonWeight(weight: Int)

    fun showPokemonHeight(height: Int)

    fun showPokemonMoves(moves: List<String>)

    fun showAddFavoriteUpdateSuccess(name: String)

    fun showRemoveFavoriteUpdateSuccess(name: String)

    fun showPreloadedFields()

    fun hideAllFields()

    fun showAllFields()

    fun toggleFavoriteCheck()

    fun showErrorOnAddFavorite(name: String)

    fun showErrorOnRemoveFavorite(name: String)

}