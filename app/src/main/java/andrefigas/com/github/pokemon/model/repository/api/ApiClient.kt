package andrefigas.com.github.pokemon.model.repository.api

import andrefigas.com.github.pokemon.model.entities.BaseEntity
import andrefigas.com.github.pokemon.model.entities.Pokemon
import andrefigas.com.github.pokemon.model.entities.Specie
import io.reactivex.Single
import repository.entities.ResultPage
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiClient {

    @GET("api/v2/pokemon")
    fun fetchPokemons(
        @Query("limit") limit: Int = 10, @Query("offset") offset: Int = 0
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
    ): Single<Specie>


}