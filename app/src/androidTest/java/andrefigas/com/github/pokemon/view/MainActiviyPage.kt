package andrefigas.com.github.pokemon.view

import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.tests.BaseTests
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers

object MainActivityPage : BasePage() {

    fun providePokemonList() =  finViewById(R.id.rv_pokemons)

    fun checkPokemonNameByPosition(position : Int, text : String): ViewInteraction = providePokemonList()
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