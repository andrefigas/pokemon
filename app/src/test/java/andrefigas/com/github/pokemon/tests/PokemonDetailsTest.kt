package andrefigas.com.github.pokemon.tests


import andrefigas.com.github.pokemon.data.DataTest
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageEvent
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageState
import andrefigas.com.github.pokemon.viewmodel.PokemonDetailsViewModel
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent

class PokemonDetailsTest : BaseUnitTests() {

    private val pokemonDetailsViewModel: PokemonDetailsViewModel by KoinJavaComponent.inject(
        PokemonDetailsViewModel::class.java, parameters = {
            parametersOf(
                DataTest.BULBASAUR
            )
        }
    )

    @Test
    fun initialLoadData() {
        pokemonDetailsViewModel.pageState.assert(
            waitFor = { data ->
                data is PokemonDetailsPageState.DetailsSuccess
            },
            assertion = { data ->
                (data as PokemonDetailsPageState.DetailsSuccess).data == DataTest.BULBASAUR_DETAILS
            },
            trigger = {
                pokemonDetailsViewModel.processEvent(PokemonDetailsPageEvent.OnCreate())
            }
        )

    }


}