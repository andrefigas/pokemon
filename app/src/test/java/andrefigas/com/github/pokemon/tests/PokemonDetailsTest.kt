package andrefigas.com.github.pokemon.tests

import andrefigas.com.github.pokemon.MockApiClient
import andrefigas.com.github.pokemon.data.DataTest
import andrefigas.com.github.pokemon.model.entities.BaseEntity
import andrefigas.com.github.pokemon.viewmodel.PokemonDetailsViewModel
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class PokemonDetailsTest : BaseUnitTests() {

    lateinit var viewModel: PokemonDetailsViewModel

    @Test
    fun initialLoadData() {

        viewModel.fetchData(null, "", null, null).test().assertValue{
            val specie = it.first
            val favoriteResponse = it.second
            return@assertValue specie.labels[0].label == DataTest.BULBASAUR_SPECIE.labels[0].label && specie.labels[0].language?.name == DataTest.BULBASAUR_SPECIE.labels[0].language?.name &&
                        specie.habitat?.name ==  DataTest.BULBASAUR_SPECIE.habitat?.name && favoriteResponse.favorite == DataTest.BULBASAUR_FAVORITE.favorite
        }

    }

    @Before
    override fun setUp() {
        super.setUp()
        networkModule = MockApiClient(
            listOf(
                DataTest.provideSpecie(DataTest.BULBASAUR_SPECIE),
                DataTest.provideFavorite(DataTest.BULBASAUR_FAVORITE)
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