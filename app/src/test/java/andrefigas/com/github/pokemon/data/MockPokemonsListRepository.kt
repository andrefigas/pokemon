package andrefigas.com.github.pokemon.data

import andrefigas.com.github.pokemon.BuildConfig
import andrefigas.com.github.pokemon.data.entities.ResultPage
import andrefigas.com.github.pokemon.domain.entities.BaseEntity
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.data.entities.PokemonListDataModel
import andrefigas.com.github.pokemon.data.repository.PokemonRepositoryContract
import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import coil.request.ImageRequest
import coil.target.Target
import io.reactivex.Single

class MockPokemonsListRepository() :
    MockRepository(mapOf(ApiClient.PokemonClient::class.java to listOf("pokemon_list.json", "bulbasaur.json"))),
    PokemonRepositoryContract {

    private lateinit var serviceClient: ApiClient.PokemonClient
    private var url: String = BuildConfig.API_URL

    override fun processClient(api: ApiClient) {
        if (api is ApiClient.PokemonClient) {
            serviceClient = api
        }
    }

    override fun providePokemons(): Single<PokemonListDataModel> =
        providePokemonList().flatMap { resultPage ->
            fetchPokemonsForPage(resultPage, url)
        }

    override fun isInitialRequest() = url == BuildConfig.API_URL

    override fun injectUrl(url: String) {
        this.url = url
    }

    override fun loadPokemonImage(pokemon: Pokemon, target: Target): ImageRequest {
        throw NotImplementedError()
    }

    private fun providePokemonList(): Single<ResultPage<BaseEntity>> {
        return if (isInitialRequest()) initialRequest() else nextRequest()
    }

    private fun fetchPokemonsForPage(
        resultPage: ResultPage<BaseEntity>,
        url: String?
    ): Single<PokemonListDataModel> {
        return Single.zip(resultPage.results.map { baseEntity ->
            fetchPokemon(baseEntity)
        }) { pokemonPages ->
            pokemonPages.map { pokemon ->
                pokemon as Pokemon
            }.toTypedArray()
        }.map { pokemons ->
            PokemonListDataModel(
                pokemons,
                resultPage,
                url
            )
        }

    }

    private fun fetchPokemon(
        baseEntity: BaseEntity
    ): Single<Pokemon> {
        var request = serviceClient.getPokemon(baseEntity.url)
            .map { pokemon ->
                pokemon.name = baseEntity.name
                pokemon.url = baseEntity.url
                pokemon
            }

        return request
    }

    private fun initialRequest() = serviceClient.fetchPokemons()

    private fun nextRequest() = serviceClient.fetchPokemons(url)

}
