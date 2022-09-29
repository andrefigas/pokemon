package andrefigas.com.github.pokemon.intent.details

sealed class PokemonDetailsPageEffect(val name : String) {

    class OnAddToFavoriteFail(name : String) : PokemonDetailsPageEffect(name)

    class OnRemoveFromFavoriteFail(name : String) : PokemonDetailsPageEffect(name)

    class OnAddToFavoriteSuccess(name : String) : PokemonDetailsPageEffect(name)

    class OnRemoveFromFavoriteSuccess(name : String) : PokemonDetailsPageEffect(name)

}