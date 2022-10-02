package andrefigas.com.github.pokemon.data.repository

import andrefigas.com.github.pokemon.BuildConfig
import andrefigas.com.github.pokemon.data.entities.FavoriteResponse
import andrefigas.com.github.pokemon.data.entities.UpdateFavoriteResponse
import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.domain.entities.Specie
import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import android.content.Context
import coil.request.ImageRequest
import coil.target.Target
import io.reactivex.Single


class PokemonDetailsRepository(context: Context, override val pokemon: Pokemon,
                               private val imageRequestBuilder : ImageRequest.Builder,
                               private val mapperContract: MapperContract) : Repository(
    context,
    mapOf(
        BuildConfig.API_URL to ApiClient.PokemonClient::class.java,
        BuildConfig.WEBHOOK_URL to ApiClient.WebHookClient::class.java
    )
), PokemonDetailsRepositoryContract{

    private lateinit var serviceClient: ApiClient.PokemonClient
    private lateinit var webHookClient: ApiClient.WebHookClient

    override fun processClient(api: ApiClient) {
        when (api) {
            is ApiClient.PokemonClient -> {
                serviceClient = api
            }

            is ApiClient.WebHookClient -> {
                webHookClient = api
            }

        }

    }

    override fun loadPokemonImage(target: Target): ImageRequest{
        val sprites = pokemon.spritesCollection
        val imageUrl = sprites.getBetterImage()
        return imageRequestBuilder
            .data(imageUrl)
            .target(target)
            .build()
    }

    override fun updateFavourite(favourite : Boolean) = webHookClient.updateFavoritePokemon(mapperContract.fromUIToData(pokemon.id, favourite))

    override fun provideSpecie() = serviceClient.getSpecie(pokemon.species.url)

    override fun provideFavourite() = webHookClient.getFavoriteByPokemon(pokemon.id)



}

interface PokemonDetailsRepositoryContract{

    val pokemon : Pokemon
    fun loadPokemonImage(target: Target): ImageRequest
    fun updateFavourite(favourite: Boolean): Single<UpdateFavoriteResponse>
    fun provideSpecie(): Single<Specie?>
    fun provideFavourite(): Single<FavoriteResponse>

}