package andrefigas.com.github.pokemon.view.entities

open class PokemonUI(name: String,
                     val weight: Int = 0,
                     val height: Int = 0,
                     val types : String,
                     val moves: String) : BaseEntity(name)
