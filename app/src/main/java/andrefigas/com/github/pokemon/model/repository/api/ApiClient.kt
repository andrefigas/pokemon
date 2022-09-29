package andrefigas.com.github.pokemon.model.repository.api

import andrefigas.com.github.pokemon.data.entities.FavoriteResponse
import andrefigas.com.github.pokemon.data.entities.ResultPage
import andrefigas.com.github.pokemon.data.entities.UpdateFavoriteResponse
import andrefigas.com.github.pokemon.domain.entities.BaseEntity
import andrefigas.com.github.pokemon.domain.entities.FavoritePokemon
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.domain.entities.Specie
import com.squareup.okhttp.ResponseBody
import io.reactivex.Single
import retrofit2.http.*

interface ApiClient {

    interface PokemonClient : ApiClient {

        companion object{
            const val LIMIT = 10
            const val OFFSET = 0
        }

        @GET("api/v2/pokemon")
        fun fetchPokemons(
            @Query("limit") limit: Int = LIMIT, @Query("offset") offset: Int = OFFSET
        ): Single<ResultPage<BaseEntity>>

        @GET
        fun fetchPokemons(
            @Url url: String
        ): Single<ResultPage<BaseEntity>>

        @GET
        fun getPokemon(
            @Url url: String
        ): Single<Pokemon>

        @GET
        fun getSpecie(
            @Url url: String
        ): Single<Specie?>

    }

    interface WebHookClient : ApiClient {

        @GET("/v3/a7aae05b-5201-4b0b-b8ea-4da512b70e02")
        fun getFavoriteByPokemon(@Query("id") id : Int) : Single<FavoriteResponse>

        @PUT("/v3/fab69dd4-bcf8-4d3b-9ed6-70d4a7e66b7d")
        fun updateFavoritePokemon(@Body favoritePokemon: FavoritePokemon) : Single<UpdateFavoriteResponse>

    }


}