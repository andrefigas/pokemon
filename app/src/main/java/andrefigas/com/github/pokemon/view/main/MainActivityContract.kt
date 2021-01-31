package andrefigas.com.github.pokemon.view.main

import andrefigas.com.github.pokemon.model.entities.Pokemon
import android.view.View

interface MainActivityContract {

    fun createPokemonList()

    fun  increasePokemonList(pokemons: List<Pokemon>)

    fun  showIncreasingDataProgress()

    fun  hideIncreasingDataProgress()

    fun  showStartingDataProgress()

    fun  hideStartingDataProgress()

    fun navigateToDetails(pokemon : Pokemon, target: View)

    fun showInitialLoadDataError()

    fun hideInitialLoadDataError()

    fun showIncreasingDataDataError()

    fun disableInfinityScroll()

}