package andrefigas.com.github.pokemon.view.main

import andrefigas.com.github.pokemon.domain.entities.Pokemon
import android.view.View

interface MainActivityContract {

    fun createPokemonList()

    fun  increasePokemonList(pokemons: Array<Pokemon>)

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