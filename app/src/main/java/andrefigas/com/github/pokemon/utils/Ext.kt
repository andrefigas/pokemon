package andrefigas.com.github.pokemon.utils

import andrefigas.com.github.pokemon.domain.entities.BaseEntity
import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.DisplayMetrics

import android.view.WindowInsets
import android.view.WindowMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException

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

fun  <T : Parcelable?> Parcel.readParcelable(loader : Class<T>): T {
    return readParcelable<T>(loader.classLoader) ?: throw IllegalArgumentException()
}

inline fun  <reified T : Parcelable?> Parcel.readParcelableArray(loader : Class<T>): Array<T> {
    return readParcelableArray(loader.classLoader)?.map { it as T }?.toTypedArray()?: throw IllegalArgumentException()
}

fun Parcel.readStringOrEmpty(): String{
    return readString()?:""
}


