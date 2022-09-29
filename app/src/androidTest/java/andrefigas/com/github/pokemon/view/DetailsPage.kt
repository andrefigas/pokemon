package andrefigas.com.github.pokemon.view

import andrefigas.com.github.pokemon.R
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click

object DetailsPage : BasePage(){

    fun checkDescription(text : String): ViewInteraction = checkText(R.id.details_description, text)

    fun checkType(text : String): ViewInteraction = checkText(R.id.details_types_values, text)

    fun checkHabitat(text : String): ViewInteraction = checkText(R.id.details_types_values, text)

    fun checkWeight(text : String): ViewInteraction = checkText(R.id.details_weight_value, text)

    fun checkHeight(text : String): ViewInteraction = checkText(R.id.details_height_value, text)

    fun checkContainsMove(text : String): ViewInteraction = DetailsPage.checkContainsText(R.id.details_moves_value, text)

    fun checkFavoriteChecked(): ViewInteraction = checkIsDisplayed(R.id.action_favorite_uncheck)

    private fun checkFavoriteUnchecked(): ViewInteraction = checkIsDisplayed(R.id.action_favorite_check)

    fun checkIsFavorite(favorite : Boolean): ViewInteraction = if (favorite) checkFavoriteChecked() else checkFavoriteUnchecked()

    fun markAsFavorite(): ViewInteraction = finViewById(R.id.action_favorite_check).perform(click())

}