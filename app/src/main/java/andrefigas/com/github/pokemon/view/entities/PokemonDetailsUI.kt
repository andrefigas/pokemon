package andrefigas.com.github.pokemon.view.entities


class PokemonDetailsUI(name: String,
                       val description : String,
                       weight: Int,
                       height: Int,
                       val habitat: String,
                       types: String,
                       moves: String,
                       val favorite: Boolean) : PokemonUI(name, weight, height, types, moves)