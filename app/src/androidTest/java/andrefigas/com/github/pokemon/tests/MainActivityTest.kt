package andrefigas.com.github.pokemon.tests

import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.model.DataTest
import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import andrefigas.com.github.pokemon.view.MainActivityPage
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseTests() {

    @Test
    fun initialLoadTest() {
        MainActivityPage.checkPokemonNameByPosition(0, DataTest.BULBASAUR_NAME)
    }

    @Test
    fun increaseDataTest() {
        MainActivityPage.swipeDownPokemonList()
        MainActivityPage.checkIsIncreaseProgressisDisplayed(DataTest.FIRST_LOAD_LAST_POSITION)
        Thread.sleep(DEFAULT_REQUEST_WAIT)
        MainActivityPage.checkPokemonNameByPosition(DataTest.FIRST_LOAD_LAST_POSITION, DataTest.CATERPIE_NAME)
    }

}