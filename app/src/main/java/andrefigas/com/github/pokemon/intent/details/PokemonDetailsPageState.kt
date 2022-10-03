package andrefigas.com.github.pokemon.intent.details

import andrefigas.com.github.pokemon.data.entities.FavoriteResponse
import andrefigas.com.github.pokemon.data.entities.PokemonDetailsDataModel
import andrefigas.com.github.pokemon.data.entities.UpdateFavoriteResponse

sealed class PokemonDetailsPageState(val data : PokemonDetailsDataModel?) {

    object Idle : PokemonDetailsPageState(null)

    class Loading(data : PokemonDetailsDataModel?) : PokemonDetailsPageState(data)

    fun updateFavoriteInProgress() = UpdateFavoriteInProgress(data)

    class UpdateFavoriteInProgress(data : PokemonDetailsDataModel?) : PokemonDetailsPageState(data)

    class UpdateFavoriteInSuccess(data : PokemonDetailsDataModel?) : PokemonDetailsPageState(data)

    class UpdateFavoriteInFail(data : PokemonDetailsDataModel?) : PokemonDetailsPageState(data)

    class DetailsSuccess(data : PokemonDetailsDataModel) : PokemonDetailsPageState(data)

    class DetailsFail(data : PokemonDetailsDataModel?): PokemonDetailsPageState(data)

    class Recycled(data : PokemonDetailsDataModel?) : PokemonDetailsPageState(data)

    //mappers
    fun loading() = Loading(data)

    fun detailsFail() = DetailsFail(data)

    fun detailsSuccess(data : PokemonDetailsDataModel) = DetailsSuccess(data)

    fun updateFavoriteInFail() = UpdateFavoriteInFail(data)

    fun updateFavoriteInSuccess(favoriteResponse: UpdateFavoriteResponse) : PokemonDetailsPageState{
        return  UpdateFavoriteInSuccess(data?.copy(favoriteResponse = FavoriteResponse(favoriteResponse.favorite)))
    }

    fun idle() = Idle

    fun recycled() = Recycled(data)

}