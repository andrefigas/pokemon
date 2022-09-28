package andrefigas.com.github.pokemon.view.entities

import andrefigas.com.github.pokemon.domain.entities.Pokemon
import android.graphics.drawable.Drawable

sealed class PokemonListData {

    open class Success(val pokemons : List<Pokemon>) : PokemonListData()

    class IncreaseSuccess(pokemons : List<Pokemon>) : Success(pokemons)

    class InitialSuccess(pokemons : List<Pokemon>) : Success(pokemons)

    object InitialError : PokemonListData()

    object IncreaseError : PokemonListData()

    class LoadImage(val data : Drawable, val pokemon : Pokemon) : PokemonListData()


}