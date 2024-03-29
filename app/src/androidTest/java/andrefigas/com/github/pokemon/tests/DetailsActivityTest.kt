package andrefigas.com.github.pokemon.tests

import andrefigas.com.github.pokemon.data.AndroidDataTest
import andrefigas.com.github.pokemon.view.DetailsPage
import andrefigas.com.github.pokemon.view.MainActivityPage
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsActivityTest : BaseTests() {

    @Before
    override fun setUp() {
        super.setUp()
        Thread.sleep(DEFAULT_REQUEST_WAIT)
        MainActivityPage.providePokemonList()
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(DEFAULT_REQUEST_WAIT)
    }

    @Test
    fun initialLoadTest() {
        DetailsPage.checkDescription(AndroidDataTest.BULBASAUR_DESCRIPTION)
        DetailsPage.checkType(AndroidDataTest.BULBASAUR_TYPE)
        DetailsPage.checkHabitat(AndroidDataTest.BULBASAUR_HABITAT)
        DetailsPage.checkWeight(AndroidDataTest.BULBASAUR_WEIGHT)
        DetailsPage.checkHeight(AndroidDataTest.BULBASAUR_HEIGHT)
        DetailsPage.checkContainsMove(AndroidDataTest.BULBASAUR_MOVE)
        DetailsPage.checkIsFavorite(AndroidDataTest.BULBASAUR_FAVORITE)
    }

    @Test
    fun favoriteTest() {
        DetailsPage.markAsFavorite()
        Thread.sleep(DEFAULT_REQUEST_WAIT)
        DetailsPage.checkFavoriteChecked()
    }


}