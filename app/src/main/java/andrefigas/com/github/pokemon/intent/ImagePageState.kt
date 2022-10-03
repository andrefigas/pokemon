package andrefigas.com.github.pokemon.intent

import android.graphics.drawable.Drawable

sealed class ImagePageState(val url : String? = null,
                            val image: Drawable? = null,
                            val placeholder : Drawable? = null,
                            val error : Drawable? = null) {

    class OnStart(drawable: Drawable?, url : String? = null) : ImagePageState(url = url, placeholder = drawable)

    class OnSuccess(drawable: Drawable , url : String? = null) : ImagePageState(url = url, image = drawable)

    class OnFail(drawable: Drawable?, url : String? = null) : ImagePageState(url = url, image = drawable)

    class Recycled(url : String? = null,
                   image: Drawable? = null,
                   placeholder : Drawable? = null,
                   error : Drawable? = null) : ImagePageState(url, image, placeholder, error)

    object Idle : ImagePageState()

    //mappers

    fun start(drawable: Drawable?, url : String? = this.url) = OnStart(drawable, url)

    fun success(drawable: Drawable, url : String? = this.url) = OnSuccess(drawable, url)

    fun fail(drawable: Drawable?, url : String? = this.url) = OnFail(drawable, url)

    fun recycle() =  Recycled(url, image, placeholder, error)

    fun idle() =  Idle

}