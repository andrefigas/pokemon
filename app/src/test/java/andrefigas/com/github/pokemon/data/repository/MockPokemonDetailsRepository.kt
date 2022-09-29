package andrefigas.com.github.pokemon.data.repository

import andrefigas.com.github.pokemon.BuildConfig
import andrefigas.com.github.pokemon.data.entities.FavoriteResponse
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.data.entities.PokemonDetailsDataModel
import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.domain.entities.Specie
import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import coil.request.ImageRequest
import coil.target.Target
import io.reactivex.Single
import io.reactivex.functions.Function


class MockPokemonDetailsRepository(private val pokemon: Pokemon, private val mapperContract: MapperContract) :
    MockRepository(
        listOf(ApiClient.PokemonClient::class.java,  ApiClient.WebHookClient::class.java),
        mapOf(
            "${BuildConfig.API_URL}api/v2/pokemon-species/1/" to "specie.json",
            "${DEFAULT_URL}v3/a7aae05b-5201-4b0b-b8ea-4da512b70e02?id=1" to "favorite.json",
            "${DEFAULT_URL}v3/fab69dd4-bcf8-4d3b-9ed6-70d4a7e66b7d" to "update_favorite.json"
        )
    ),
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
       throw NotImplementedError()
    }

    override fun updateFavourite(favourite : Boolean) = webHookClient.updateFavoritePokemon(mapperContract.fromUIToData(pokemon.id, favourite))

    private fun provideSpecie() = serviceClient.getSpecie(pokemon.species?.url?:"")//todo fix

    private fun provideFavourite() = webHookClient.getFavoriteByPokemon(pokemon.id)

}
