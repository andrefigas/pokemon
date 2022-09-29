package andrefigas.com.github.pokemon.utils

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

