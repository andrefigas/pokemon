package andrefigas.com.github.pokemon.tests


import andrefigas.com.github.pokemon.data.DataTest
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import org.junit.Test
import org.koin.java.KoinJavaComponent.inject

class PokemonListTest : BaseUnitTests() {

    private val pokemonListViewModel: PokemonListViewModel by inject (PokemonListViewModel::class.java)

    @Test
    fun initialLoadData() {

        pokemonListViewModel.initialLoad.assert(
            { data ->
                data.pokemons.contentEquals(arrayOf(DataTest.BULBASAUR))
            },
            {
                pokemonListViewModel.fetchData()
            }
        )

    }


}