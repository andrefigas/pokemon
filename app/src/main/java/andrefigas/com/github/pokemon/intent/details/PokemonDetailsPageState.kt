package andrefigas.com.github.pokemon.intent.details

import andrefigas.com.github.pokemon.data.entities.PokemonDetailsDataModel

sealed class PokemonDetailsPageState {

    object Loading : PokemonDetailsPageState()

    object UpdateFavoriteInProgress : PokemonDetailsPageState()

    class DetailsSuccess(val data : PokemonDetailsDataModel) : PokemonDetailsPageState()

    object DetailsFail: PokemonDetailsPageState()


}