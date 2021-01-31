package andrefigas.com.github.pokemon.model.entities

class PokemonDetailsDataModel(val pokemon: Pokemon) {
    var initted: Boolean = false
    var species: Specie? = null
    var favoriteResponse: FavoriteResponse? = null
}