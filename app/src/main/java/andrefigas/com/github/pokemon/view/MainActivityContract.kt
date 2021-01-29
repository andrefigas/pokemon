package andrefigas.com.github.pokemon.view

import andrefigas.com.github.pokemon.model.entities.Pokemon

interface MainActivityContract {

    fun createPokemonList()

    fun  increasePokemonList(pokemons: List<Pokemon>)

    fun  showIncreasingDataProgress()

    fun  hideIncreasingDataProgress()

    fun  showStartingDataProgress()

    fun  hideStartingDataProgress()

}