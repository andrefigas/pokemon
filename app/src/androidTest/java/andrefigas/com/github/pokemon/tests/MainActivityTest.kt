package andrefigas.com.github.pokemon.tests

import andrefigas.com.github.pokemon.data.DataTest
import andrefigas.com.github.pokemon.view.MainActivityPage
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith


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

        MainActivityPage.checkPokemonNameByPosition(
            DataTest.FIRST_LOAD_LAST_POSITION,
            DataTest.CATERPIE_NAME
        )
    }

}