package andrefigas.com.github.pokemon.view

import andrefigas.com.github.pokemon.R
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers

object DetailsPage : BasePage(){

    fun checkDescription(text : String) = checkText(R.id.details_description, text)

    fun checkType(text : String) = checkText(R.id.details_types_values, text)

    fun checkHabitat(text : String) = checkText(R.id.details_types_values, text)

    fun checkWeight(text : String) = checkText(R.id.details_weight_value, text)

    fun checkHeight(text : String) = checkText(R.id.details_height_value, text)

    fun checkContainsMove(text : String) = DetailsPage.checkContainsText(R.id.details_moves_value, text)

    fun checkFavoriteChecked() = checkIsDisplayed(R.id.action_favorite_uncheck)

    fun checkFavoriteUnchecked() = checkIsDisplayed(R.id.action_favorite_check)

    fun checkIsFavorite(favorite : Boolean) = if (favorite) checkFavoriteChecked() else checkFavoriteUnchecked()

    fun markAsFavorite() = finViewById(R.id.action_favorite_check).perform(click())

}