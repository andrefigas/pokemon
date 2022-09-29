package andrefigas.com.github.pokemon.tests


import andrefigas.com.github.pokemon.data.DataTest
import andrefigas.com.github.pokemon.intent.list.PokemonListPageEvent
import andrefigas.com.github.pokemon.intent.list.PokemonListPageState
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import org.junit.Test
import org.koin.java.KoinJavaComponent.inject

class PokemonListTest : BaseUnitTests() {

    private val pokemonListViewModel: PokemonListViewModel by inject (PokemonListViewModel::class.java)

    @Test
    fun initialLoadData() {

        pokemonListViewModel.pageState.assert(
            waitFor = { data ->
                data is PokemonListPageState.InitialSuccess
            },
            assertion = { data ->
                (data as PokemonListPageState.InitialSuccess).pokemons.contentEquals(arrayOf(DataTest.BULBASAUR))
            },
            trigger = {
                pokemonListViewModel.processEvent(PokemonListPageEvent.OnCreate)
            }
        )

    }


}