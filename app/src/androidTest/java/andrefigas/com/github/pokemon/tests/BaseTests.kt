package andrefigas.com.github.pokemon.tests

import andrefigas.com.github.pokemon.injection.modules.NetworkModule
import andrefigas.com.github.pokemon.view.main.MainActivity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule

abstract class BaseTests {

    companion object{

        val DEFAULT_REQUEST_WAIT = 10000L

        fun swipeFromTopToBottom(): ViewAction? {
            return GeneralSwipeAction(
                Swipe.FAST, GeneralLocation.BOTTOM_CENTER,
                GeneralLocation.TOP_CENTER, Press.FINGER
            )
        }

        fun atPositionOnView(
            position: Int, itemMatcher: Matcher<View?>,
            targetViewId: Int
        ): Matcher<View?>? {
            return object :
                BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description) {
                    description.appendText("has view id $itemMatcher at position $position")
                }

                override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                    val viewHolder =
                        recyclerView.findViewHolderForAdapterPosition(position)
                    val targetView =
                        viewHolder?.itemView?.findViewById<View>(targetViewId) ?:false
                    return itemMatcher.matches(targetView)
                }
            }
        }

    }

    @JvmField
    @Rule
    val activityRule = ActivityTestRule<MainActivity>(
        MainActivity::class.java, true, false
    )

    @Before
    open fun setUp() {
        NetworkModule.provideCacheFile(InstrumentationRegistry.getInstrumentation().targetContext)
            .listFiles().forEach {
                it.delete()
            }

        activityRule.launchActivity(null)
        Thread.sleep(DEFAULT_REQUEST_WAIT)
    }


}