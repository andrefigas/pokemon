package andrefigas.com.github.pokemon.utils

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics

import android.view.WindowInsets
import android.view.WindowMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Single

/**
 *EXTENSION FUNCTIONS
 */

/**
 * infinity scroll
 */
fun RecyclerView.doOnScrollEnding(
    offset: Int,
    onScrollEnding: () -> Unit,
    isProgressing: () -> Boolean
): RecyclerView.OnScrollListener? {

    val adapter = adapter
    val layoutManager = layoutManager
    if (layoutManager is LinearLayoutManager && adapter != null) {

        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)

                val lastPosition = adapter.itemCount - 1
                val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
                if (dy > 0 && !isProgressing.invoke() && adapter.itemCount > 0 && lastPosition - lastVisiblePosition <= offset) {
                    onScrollEnding.invoke()
                }

            }
        }

        addOnScrollListener(listener)

        return listener

    } else {
        return null
    }

}

fun Activity.getDisplayWidth(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.width() - insets.left - insets.right
    } else {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    }
}

/**
 * build a string with data list and a string separator
 * @param separator splitter
 */
fun List<String>.toString(separator: String): String {
    if (size == 0) {
        return ""
    }

    val sb = StringBuilder()

    forEach {
        sb.append(it)
        sb.append(separator)
    }

    sb.delete(sb.length - separator.length, sb.length)
    return sb.toString()
}

/**
 * getting a request and make a pair with a response from another request
 */
fun <I, O> Single<I>.pair(
    b: Single<O>
): Single<Pair<I, O>> {
    return flatMap { i ->
        b.map { o ->
            Pair(i, o)
        }
    }
}

