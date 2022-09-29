package andrefigas.com.github.pokemon.tests

import andrefigas.com.github.pokemon.view.main.MainActivity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule

abstract class BaseTests {

    companion object {

        const val DEFAULT_REQUEST_WAIT = 10000L

        fun atPositionOnView(
            position: Int, itemMatcher: Matcher<View?>,
            targetViewId: Int
        ): Matcher<View?> {
            return object :
                BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description) {
                    description.appendText("has view id $itemMatcher at position $position")
                }

                override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                    val viewHolder =
                        recyclerView.findViewHolderForAdapterPosition(position)
                    val targetView =
                        viewHolder?.itemView?.findViewById<View>(targetViewId) ?: false
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
        activityRule.launchActivity(null)
        Thread.sleep(DEFAULT_REQUEST_WAIT)
    }


}