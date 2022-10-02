package andrefigas.com.github.pokemon.data.repository

import andrefigas.com.github.pokemon.BuildConfig
import andrefigas.com.github.pokemon.data.entities.ResultPage
import andrefigas.com.github.pokemon.domain.entities.BaseEntity
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import android.content.Context
import coil.request.ImageRequest
import coil.target.Target
import io.reactivex.Single

class PokemonRepository(
    context: Context, private val imageRequestBuilder: ImageRequest.Builder
) :
    Repository(context, mapOf(BuildConfig.API_URL to ApiClient.PokemonClient::class.java)),
    PokemonRepositoryContract {

    private lateinit var serviceClient: ApiClient.PokemonClient
    private var url: String = BuildConfig.API_URL

    override fun processClient(api: ApiClient) {
        if (api is ApiClient.PokemonClient) {
            serviceClient = api
        }
    }

    fun injectUrl(url: String?) {
        this.url = url ?: BuildConfig.API_URL
    }

    override fun loadPokemonImage(pokemon: Pokemon, target: Target): ImageRequest {
        val sprites = pokemon.spritesCollection
        val imageUrl = sprites.getBetterImage()
        return imageRequestBuilder
            .data(imageUrl)
            .target(target)
            .build()
    }

    override fun fetchInitialPokemonsPage(): Single<ResultPage<BaseEntity>> = serviceClient.fetchPokemons().map {page->
        injectUrl(page.next)
        page
    }

    override fun fetchNextPokemonsPage(): Single<ResultPage<BaseEntity>> = serviceClient.fetchPokemons(url).map {page->
        injectUrl(page.next)
        page
    }

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

interface PokemonRepositoryContract {

    fun loadPokemonImage(pokemon: Pokemon, target: Target): ImageRequest
    fun fetchInitialPokemonsPage(): Single<ResultPage<BaseEntity>>
    fun fetchNextPokemonsPage(): Single<ResultPage<BaseEntity>>
    fun fetchPokemon(baseEntity: BaseEntity): Single<Pokemon>

}