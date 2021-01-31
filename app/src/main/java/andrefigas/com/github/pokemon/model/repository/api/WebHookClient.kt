package andrefigas.com.github.pokemon.model.repository.api

import andrefigas.com.github.pokemon.model.entities.FavoritePokemon
import andrefigas.com.github.pokemon.model.entities.FavoriteResponse
import andrefigas.com.github.pokemon.model.entities.UpdateFavoriteResponse
import io.reactivex.Single
import retrofit2.http.*

/**
 * Client for access and change (without effetct) favorite services
 */
interface WebHookClient {

    @GET("/073c48f8-ead8-4daf-a49a-a8552b5af628")
    fun getFavoriteByPokemon(@Query("id") id : Int) : Single<FavoriteResponse>

    @PUT("/984f33a0-e4ab-4f90-88cf-255ac7c8b5bb")
    fun updateFavoritePokemon(@Body favoritePokemon: FavoritePokemon) : Single<UpdateFavoriteResponse>

}