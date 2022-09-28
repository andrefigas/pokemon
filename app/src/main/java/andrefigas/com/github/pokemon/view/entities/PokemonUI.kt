package andrefigas.com.github.pokemon.view.entities

open class PokemonUI(name: String,
                     val weight: Int = 0,
                     val height: Int = 0,
                     val photo: String,
                     val types : List<String>,
                     val moves: List<String>) : BaseEntity(name)
