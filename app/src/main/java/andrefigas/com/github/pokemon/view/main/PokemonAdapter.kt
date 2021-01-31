package andrefigas.com.github.pokemon.view.main

import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.model.entities.Pokemon
import andrefigas.com.github.pokemon.utils.ImageUtils
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.request.Disposable
import coil.target.Target
import kotlinx.android.synthetic.main.pokemon_item.view.*
import java.util.*


class PokemonAdapter(val mainActivityContract: MainActivityContract) :
    RecyclerView.Adapter<ViewHolder>() {
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
                    .inflate(R.layout.pokemon_item, parent, false),
                mainActivityContract
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
        if (pokemons.size == pokemonList.size) {
            return
        }

        val startIndex = itemCount
        val count = pokemons.size
        pokemonList.addAll(pokemons.subList(startIndex, count - 1))
        notifyItemRangeInserted(startIndex, count)
    }

    fun setProgressing(progressing: Boolean) {
        if (progressing == isProgressing) return

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

    class ItemViewHolder(itemView: View, val mainActivityContract: MainActivityContract) :
        ViewHolder(itemView), Target {
        lateinit var pokemon: Pokemon
        var disposable: Disposable? = null

        fun drawView() {

            itemView.setOnClickListener {
                mainActivityContract.navigateToDetails(pokemon, itemView.pokemon_item_image)
            }

            val tvName = itemView.pokemon_item_name
            tvName.text = pokemon.name.capitalize()

            disposable = ImageUtils.loadPokemonImage(itemView.context, pokemon, this)
        }

        fun unload() {
            disposable?.dispose()
        }

        override fun onSuccess(result: Drawable) {
            itemView.pokemon_item_image.setImageDrawable(result)
        }


        override fun onStart(placeholder: Drawable?) {
            itemView.pokemon_item_image.setImageDrawable(placeholder)
        }

        override fun onError(error: Drawable?) {
            itemView.pokemon_item_image.setImageDrawable(error)
        }

    }

    class ProgressViewHolder(itemView: View) : ViewHolder(itemView)
    companion object {
        const val ITEM_TYPE = 1
        const val PROGRESS_TYPE = 2
    }
}