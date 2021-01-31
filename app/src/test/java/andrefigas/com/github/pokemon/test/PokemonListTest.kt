package andrefigas.com.github.pokemon.test

import andrefigas.com.github.pokemon.MockApiClient
import andrefigas.com.github.pokemon.data.DataTest
import andrefigas.com.github.pokemon.model.entities.BaseEntity
import andrefigas.com.github.pokemon.model.entities.Pokemon
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class PokemonListTest {

    lateinit var networkModule: MockApiClient
    lateinit var pokeViewModel: PokemonListViewModel

    @Test
    fun initialLoadData() {
        pokeViewModel.fetchResultPage(null, null, null).test().assertValue { pokeViewModel ->
            val pokemon: Pokemon = pokeViewModel.pokemons[0]
            return@assertValue pokemon.height == DataTest.BULBASAUR.height && pokemon.weight == DataTest.BULBASAUR.weight
                    && pokemon.id == pokemon.id && pokemon.name == DataTest.BULBASAUR.name
        }

    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this);
        networkModule = MockApiClient()
        pokeViewModel = object : PokemonListViewModel(networkModule) {
            override fun provideUrl(baseEntity: BaseEntity): String {
                return networkModule.webServer.url("/").host
            }
        }
    }

    @After
    fun tearDown() {
        networkModule.webServer.close()
    }


}