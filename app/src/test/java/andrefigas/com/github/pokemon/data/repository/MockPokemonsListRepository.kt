package andrefigas.com.github.pokemon.data.repository

import andrefigas.com.github.pokemon.BuildConfig
import andrefigas.com.github.pokemon.data.entities.ResultPage
import andrefigas.com.github.pokemon.domain.entities.BaseEntity
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import coil.request.ImageRequest
import coil.target.Target
import io.reactivex.Single

class MockPokemonsListRepository:
    MockRepository(
        listOf(ApiClient.PokemonClient::class.java),
        mapOf(
            "${DEFAULT_URL}api/v2/pokemon?limit=10&offset=0" to "pokemon_list.json",
            "${BuildConfig.API_URL}api/v2/pokemon/1/" to "bulbasaur.json"
        )
    ),
    PokemonRepositoryContract {

    private lateinit var serviceClient: ApiClient.PokemonClient
    private var url: String = BuildConfig.API_URL

    override fun processClient(api: ApiClient) {
        if (api is ApiClient.PokemonClient) {
            serviceClient = api
        }
    }

    override fun injectUrl(url: String?) {
        this.url = url ?: BuildConfig.API_URL
    }

    override fun loadPokemonImage(pokemon: Pokemon, target: Target): ImageRequest {
       throw NotImplementedError()
    }

    override fun fetchInitialPokemonsPage(): Single<ResultPage<BaseEntity>> = serviceClient.fetchPokemons()

    override fun fetchNextPokemonsPage(): Single<ResultPage<BaseEntity>> =serviceClient.fetchPokemons(url)

    override fun fetchPokemon(
        baseEntity: BaseEntity
    ): Single<Pokemon> {
        val request = serviceClient.getPokemon(baseEntity.url)
            .map { pokemon ->
                pokemon.copy(name = baseEntity.name, url = baseEntity.url)
            }

        return request
    }


}
