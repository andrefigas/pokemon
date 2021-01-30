package andrefigas.com.github.pokemon

import andrefigas.com.github.pokemon.Utils.atPositionOnView
import andrefigas.com.github.pokemon.view.main.MainActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @JvmField
    @Rule
    val activityRule = ActivityTestRule<MainActivity>(
        MainActivity::class.java)

    @Test
    fun initialLoadTest() {
        Thread.sleep(10000L)
        onView(withId(R.id.rv_pokemons))
            .check(matches(atPositionOnView(0, withText("bulbasaur"), R.id.pokemon_item_name)));
    }

    @Test
    fun increaseDataTest() {
        Thread.sleep(10000L)

        onView(withId(R.id.rv_pokemons)).perform(Utils.swipeFromTopToBottom())

        onView(withId(R.id.rv_pokemons))
            .check(matches(atPositionOnView(10, isDisplayed(), R.id.list_item_progress)))
    }


    fun goToDetailsActivity() {
        /*onView(withId(R.id.rv_pokemons))
         .perform(RecyclerViewActions.actionOnItemAtPosition<PokemonAdapter.ItemViewHolder>(0, click()))*/
    }


}