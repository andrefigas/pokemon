package andrefigas.com.github.pokemon.intent

import android.graphics.drawable.Drawable

sealed class ImagePageState(val drawable: Drawable?, val url : String? = null) {

    class OnStart( drawable: Drawable?, url : String? = null) : ImagePageState(drawable, url)

    class OnSuccess(drawable: Drawable , url : String? = null) : ImagePageState(drawable, url)

    class OnFail(drawable: Drawable?, url : String? = null) : ImagePageState(drawable, url)

}