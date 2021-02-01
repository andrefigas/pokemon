package andrefigas.com.github.pokemon.tests

import andrefigas.com.github.pokemon.model.DataTest
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
        MainActivityPage.providePokemonList()
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(DEFAULT_REQUEST_WAIT)
    }

    @Test
    fun initialLoadTest() {
        DetailsPage.checkDescription(DataTest.BULBASAUR_DESCRIPTION)
        DetailsPage.checkType(DataTest.BULBASAUR_TYPE)
        DetailsPage.checkHabitat(DataTest.BULBASAUR_HABITAT)
        DetailsPage.checkWeight(DataTest.BULBASAUR_WEIGHT)
        DetailsPage.checkHeight(DataTest.BULBASAUR_HEIGHT)
        DetailsPage.checkContainsMove(DataTest.BULBASAUR_MOVE)
        DetailsPage.checkIsFavorite(DataTest.BULBASAUR_FAVORITE)
    }

    @Test
    fun favoriteTest() {
        DetailsPage.markAsFavorite()
        DetailsPage.checkFavoriteChecked()
    }


}