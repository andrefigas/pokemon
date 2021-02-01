package andrefigas.com.github.pokemon.tests

import andrefigas.com.github.pokemon.MockApiClient
import andrefigas.com.github.pokemon.data.DataTest
import andrefigas.com.github.pokemon.model.entities.BaseEntity
import andrefigas.com.github.pokemon.model.entities.Pokemon
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class PokemonListTest : BaseUnitTests() {

    lateinit var pokeViewModel: PokemonListViewModel

    @Test
    fun initialLoadData() {
        pokeViewModel.fetchResultPage(null, null, null).test().assertValue { pokeViewModel ->
            val pokemon: Pokemon = pokeViewModel.pokemons[0]
            return@assertValue pokemon.height == DataTest.BULBASAUR.height && pokemon.weight == DataTest.BULBASAUR.weight
                    && pokemon.id == pokemon.id && pokemon.name == DataTest.BULBASAUR.name
        }

    }

    @Test
    fun increaseLoadData() {
        pokeViewModel.fetchResultPage(null, null, null).flatMap {
            pokeViewModel.fetchResultPage(null, null, null)
        }.test().assertValue {pokeViewModel ->
            val pokemon: Pokemon = pokeViewModel.pokemons[0]
            return@assertValue pokemon.height == DataTest.IVYSAUR.height && pokemon.weight == DataTest.IVYSAUR.weight
                    && pokemon.id == pokemon.id && pokemon.name == DataTest.IVYSAUR.name
        }

    }

    @Before
    override fun setUp() {
        super.setUp()
        networkModule = MockApiClient(
            listOf(
                DataTest.providePage(offset = 0, limit = 1, results = listOf(DataTest.BULBASAUR_ENTITY)),
                DataTest.providePokemon(DataTest.BULBASAUR),
                DataTest.providePage(offset = 1, limit = 1, results = listOf(DataTest.IVYSAUR_ENTITY)),
                DataTest.providePokemon(DataTest.IVYSAUR)
            )
        )
        pokeViewModel = object : PokemonListViewModel(networkModule) {
            override fun provideUrl(baseEntity: BaseEntity): String {
                return networkModule.webServer.url("/").host
            }
        }
    }

}