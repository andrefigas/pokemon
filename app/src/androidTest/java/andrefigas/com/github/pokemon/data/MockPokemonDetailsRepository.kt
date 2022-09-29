package andrefigas.com.github.pokemon.data

import andrefigas.com.github.pokemon.BuildConfig
import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.data.entities.FavoriteResponse
import andrefigas.com.github.pokemon.data.entities.UpdateFavoriteResponse
import andrefigas.com.github.pokemon.domain.entities.FavoritePokemon
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.data.entities.PokemonDetailsDataModel
import andrefigas.com.github.pokemon.data.mappers.MapperContract
import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepositoryContract
import andrefigas.com.github.pokemon.domain.entities.Specie
import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import android.content.Context
import coil.request.ImageRequest
import coil.target.Target
import io.reactivex.Single
import io.reactivex.functions.Function


class MockPokemonDetailsRepository(val pokemon: Pokemon,
                                   val mapperContract: MapperContract,
                                   private val imageRequestBuilder : ImageRequest.Builder) :
    MockRepository(
        mapOf(
            ApiClient.PokemonClient::class.java to listOf("specie.json"),
            ApiClient.WebHookClient::class.java to listOf("favorite.json")
        )
    )
    ,
    PokemonDetailsRepositoryContract {

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
                val details = PokemonDetailsDataModel(pokemon)
                results.forEach {  any ->
                    when(any){
                        is Specie->{
                            details.species = any
                        }

                        is FavoriteResponse->{
                            details.favoriteResponse = any
                        }
                    }
                }

                details
            }

        return Single.zip(
            request,
            zipper
        )

    }

    override fun loadPokemonImage(target: Target): ImageRequest{
        return imageRequestBuilder.data(R.drawable.ic_pokeball).target(target).build()
    }

    override fun updateFavourite(favourite : Boolean) = webHookClient.updateFavoritePokemon(mapperContract.fromUIToData(pokemon.id, favourite))

    private fun provideSpecie() = serviceClient.getSpecie(pokemon.species?.url?:"")//todo fix

    private fun provideFavourite() = webHookClient.getFavoriteByPokemon(pokemon.id)

}
