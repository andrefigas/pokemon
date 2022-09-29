package andrefigas.com.github.pokemon.view

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString

open class BasePage{

    fun checkText(@IdRes idRes: Int, text : String): ViewInteraction = onView(allOf(withId(idRes), withText(text)))

    fun checkContainsText(@IdRes idRes: Int, text : String): ViewInteraction = onView(allOf(withId(idRes), withText(containsString(text))))

    fun checkIsDisplayed(@IdRes idRes: Int): ViewInteraction = finViewById(idRes).check(matches(isDisplayed()))

    fun finViewById(@IdRes idRes: Int): ViewInteraction = onView(
        withId(
            idRes
        ))
}