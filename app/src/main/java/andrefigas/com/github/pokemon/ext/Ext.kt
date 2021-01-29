package andrefigas.com.github.pokemon.ext

import android.app.Activity
import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowMetrics
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest

fun RecyclerView.doOnScrollEnding(offset : Int, onScrollEnding : ()-> Unit, isProgressing : ()->Boolean) : RecyclerView.OnScrollListener?{

    val adapter = adapter
    val layoutManager = layoutManager
    if(layoutManager is LinearLayoutManager && adapter != null){

        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)

                val lastPosition = adapter.itemCount -1
                val lastVisiblePosition =  layoutManager.findLastCompletelyVisibleItemPosition()
                if(dy > 0 && !isProgressing.invoke() && adapter.itemCount > 0 && lastPosition - lastVisiblePosition  <= offset){
                    onScrollEnding.invoke()
                }

            }
        }

        addOnScrollListener(listener)

        return listener

    }else{
        return null
    }

}

fun Activity.getDisplayWidth(): Int{
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

fun ImageView.loadSvg(context : Context, url: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(context)) }
        .build()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .data(url)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}