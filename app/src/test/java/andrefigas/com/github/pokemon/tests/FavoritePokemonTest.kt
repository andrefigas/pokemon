package andrefigas.com.github.pokemon.tests

import andrefigas.com.github.pokemon.MockApiClient
import andrefigas.com.github.pokemon.data.DataTest
import andrefigas.com.github.pokemon.model.entities.BaseEntity
import andrefigas.com.github.pokemon.viewmodel.PokemonDetailsViewModel
import org.junit.Before
import org.junit.Test

class FavoritePokemonTest : BaseUnitTests() {

    lateinit var viewModel: PokemonDetailsViewModel

    @Test
    fun initialLoadData() {
        viewModel.updateFavoriteRequest(null, !DataTest.BULBASAUR_FAVORITE.favorite, null, null)
            .test().assertValue {
            return@assertValue it.message == DataTest.UPDATE_MESSAGE
        }

    }

    @Before
    override fun setUp() {
        super.setUp()
        networkModule = MockApiClient(
            listOf(
                DataTest.provideUpdateFavoriteResponse(DataTest.UPDATE_MESSAGE)
            )
        )

        viewModel = object : PokemonDetailsViewModel(networkModule) {

            override fun provideUrl(specie: BaseEntity): String {
                return networkModule.webServer.url("/").host
            }
        }

        viewModel.pokemon = DataTest.BULBASAUR
    }

}