package andrefigas.com.github.pokemon.data.repository

import andrefigas.com.github.pokemon.BuildConfig
import andrefigas.com.github.pokemon.data.entities.FavoriteResponse
import andrefigas.com.github.pokemon.data.entities.UpdateFavoriteResponse
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.data.entities.PokemonDetailsDataModel
import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.domain.entities.Specie
import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import android.content.Context
import coil.request.ImageRequest
import coil.target.Target
import io.reactivex.Single
import io.reactivex.functions.Function


class PokemonDetailsRepository(context: Context, val pokemon: Pokemon,
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

    override fun provideDetails(): Single<PokemonDetailsDataModel> {
        val request = listOf(
            provideSpecie(),
            provideFavourite()
        )

        val zipper : Function<Array<Any>, PokemonDetailsDataModel> =
            Function{ results ->
                var specie : Specie? = null
                var favorite : FavoriteResponse? = null

                results.forEach {  any ->
                    when(any){
                        is Specie->{
                            specie =  any
                        }

                        is FavoriteResponse->{
                            favorite = any
                        }
                    }
                }

                PokemonDetailsDataModel(pokemon,
                    specie as Specie,
                    favorite as FavoriteResponse)
            }

        return Single.zip(
            request,
            zipper
        )

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

    private fun provideSpecie() = serviceClient.getSpecie(pokemon.species.url)

    private fun provideFavourite() = webHookClient.getFavoriteByPokemon(pokemon.id)

}

interface PokemonDetailsRepositoryContract{

    fun provideDetails(): Single<PokemonDetailsDataModel>
    fun loadPokemonImage(target: Target): ImageRequest
    fun updateFavourite(favourite: Boolean): Single<UpdateFavoriteResponse>
}