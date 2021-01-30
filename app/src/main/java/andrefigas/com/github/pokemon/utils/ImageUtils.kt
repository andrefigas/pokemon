package andrefigas.com.github.pokemon.utils

import andrefigas.com.github.pokemon.model.entities.Pokemon
import android.content.Context
import android.graphics.drawable.Drawable
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.Disposable
import coil.request.ImageRequest
import coil.target.Target

object ImageUtils {

    fun loadPokemonImage(context : Context, pokemon : Pokemon, target: Target): Disposable? {
        val sprites = pokemon.spritesCollection
        if (sprites != null) {
            val imageUrl = sprites.getBetterImage()
            if (imageUrl != null) {

                val imageLoader = ImageLoader.Builder(context)
                    .componentRegistry { add(SvgDecoder(context)) }
                    .build()

                val request = ImageRequest.Builder(context)
                    .crossfade(true)
                    .crossfade(500)
                    .data(imageUrl)
                    .target(target)
                    .build()

                return imageLoader.enqueue(request)

            }

        }

        return null

    }
}