package andrefigas.com.github.pokemon.model.repository.api

import io.reactivex.Single
import repository.entities.ResultPage
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiClient {

    @GET("api/v2/pokemon")
    fun fetchPokemons(
        @Query("limit") limit: String, @Query("offset") offset: String
    ): Single<ResultPage>

    @GET
    fun fetchPokemons(
        @Url url: String
    ): Single<ResultPage>


}