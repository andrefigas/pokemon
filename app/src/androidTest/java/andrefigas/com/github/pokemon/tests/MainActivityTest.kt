package andrefigas.com.github.pokemon.tests

import andrefigas.com.github.pokemon.data.AndroidDataTest
import andrefigas.com.github.pokemon.view.MainActivityPage
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseTests() {

    @Test
    fun initialLoadTest() {
        MainActivityPage.checkPokemonNameByPosition(0, AndroidDataTest.BULBASAUR_NAME)
    }

}