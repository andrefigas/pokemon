package andrefigas.com.github.pokemon.view

import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.tests.BaseTests
import androidx.annotation.IdRes
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers

object MainActivityPage : BasePage() {

    fun providePokemonList() =  finViewById(R.id.rv_pokemons)

    fun checkPokemonNameByPosition(position : Int, text : String) = providePokemonList()
        .check(
            ViewAssertions.matches(
                BaseTests.atPositionOnView(
                    position,
                    ViewMatchers.withText(text),
                    R.id.pokemon_item_name
                )
            )
        )

}