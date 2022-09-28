package andrefigas.com.github.pokemon.view.entities


class PokemonDetailsUI(name: String,
                       val description : String,
                       weight: Int = 0,
                       height: Int = 0,
                       photo: String,
                       val habitat: String,
                       types: List<String>,
                       moves: List<String>,
                       val favorite: Boolean) : PokemonUI(name, weight, height, photo, types, moves)