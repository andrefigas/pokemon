package andrefigas.com.github.pokemon.view

import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.ext.loadSvg
import andrefigas.com.github.pokemon.model.entities.Pokemon
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.Disposable
import coil.request.ImageRequest
import coil.target.Target
import coil.util.CoilUtils
import java.util.*

class PokemonAdapter : RecyclerView.Adapter<ViewHolder>() {
    val pokemonList: MutableList<Pokemon> = ArrayList()
    private var isProgressing = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            PROGRESS_TYPE -> ProgressViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.pokemon_item_progressing, parent, false)
            )
            ITEM_TYPE -> ItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.pokemon_item, parent, false)
            )
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.pokemon = pokemonList[position]
        }
    }

    override fun getItemCount(): Int {
        return if (isProgressing) pokemonList.size + 1 else pokemonList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isProgressing && position == itemCount - 1) PROGRESS_TYPE else ITEM_TYPE
    }

    fun addPokemons(pokemons: List<Pokemon>) {
        val startIndex = itemCount
        val count = pokemons.size
        pokemonList.addAll(pokemons)
        notifyItemRangeInserted(startIndex, count)
    }

    fun setProgressing(progressing: Boolean) {
        if (progressing) {
            isProgressing = true
            notifyItemInserted(itemCount)
        } else {
            notifyItemRemoved(itemCount)
            isProgressing = false
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is ItemViewHolder) {
            holder.drawView()
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is ItemViewHolder) {
            holder.unload()
        }
    }

    class ItemViewHolder(itemView: View) : ViewHolder(itemView), Target {
        lateinit var pokemon: Pokemon
        var disposable : Disposable? = null

        fun drawView() {

           itemView.setOnClickListener {
               Toast.makeText(itemView.context, pokemon.name, Toast.LENGTH_SHORT).show()
           }

            val tvName = itemView.findViewById<TextView>(R.id.pokemon_item_name)
            tvName.text = pokemon.name
            val sprites = pokemon.spritesCollection
            if (sprites != null) {
                val imageUrl = sprites.getBetterImage()
                if (imageUrl != null) {
                    val imageView = itemView.findViewById<ImageView>(R.id.pokemon_item_image)
                    imageView.loadSvg(itemView.context, imageUrl)

                    val imageLoader = ImageLoader.Builder(imageView.context)
                        .componentRegistry { add(SvgDecoder(imageView.context)) }
                        .build()

                    val request = ImageRequest.Builder(imageView.context)
                        .crossfade(true)
                        .crossfade(500)
                        .data(imageUrl)
                        .target(this)
                        .build()

                    disposable = imageLoader.enqueue(request)

                }
            }
        }

        fun unload(){
            disposable?.dispose()
        }

        override fun onSuccess(result: Drawable) {
            val imageView = itemView.findViewById<ImageView>(R.id.pokemon_item_image)
            imageView.setImageDrawable(result)
        }


    }

    class ProgressViewHolder(itemView: View) : ViewHolder(itemView)
    companion object {
        const val ITEM_TYPE = 1
        const val PROGRESS_TYPE = 2
    }
}