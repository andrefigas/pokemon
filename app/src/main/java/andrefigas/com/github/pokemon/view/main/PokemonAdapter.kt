package andrefigas.com.github.pokemon.view.main

import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.intent.ImagePageState
import andrefigas.com.github.pokemon.intent.list.PokemonListPageEvent
import andrefigas.com.github.pokemon.utils.IntentArgsUtils
import andrefigas.com.github.pokemon.view.details.DetailsActivity
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.pokemon_item.view.*


class PokemonAdapter(
    private val viewModel: PokemonListViewModel,
    private val options: (View) -> ActivityOptionsCompat) :
    RecyclerView.Adapter<ViewHolder>() {

    private val pokemonList: MutableList<Pokemon> = ArrayList()
    private var isProgressing = false

    lateinit var context: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

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
            val pokemon = pokemonList[position]
            holder.pokemon = pokemon
            holder.itemView.setOnClickListener {
                //animation
                ContextCompat.startActivity(
                    holder.itemView.context,
                    IntentArgsUtils.putPokemonInArgs(
                        Intent(
                            holder.itemView.context,
                            DetailsActivity::class.java
                        ), pokemon
                    ), options(holder.itemView).toBundle()
                )

            }
        }
    }

    override fun getItemCount(): Int {
        return if (isProgressing) pokemonList.size + 1 else pokemonList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isProgressing && position == itemCount - 1) PROGRESS_TYPE else ITEM_TYPE
    }

    fun addPokemons(pokemons: List<Pokemon>) {
        if (pokemons.isEmpty()) {
            return
        }

        val startIndex = itemCount
        val endIndex = pokemons.size
        val incrementSize = endIndex - startIndex
        pokemonList.addAll(pokemons.subList(startIndex, endIndex))
        notifyItemRangeInserted(startIndex, incrementSize)
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
            holder.drawView(viewModel.mapperContract)
            holder.subscribe(viewModel.imageState)
            viewModel.processEvent(PokemonListPageEvent.OnLoadCard(holder.itemView.context, holder.pokemon))
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is ItemViewHolder) {
            holder.unsubscribe()
        }
    }

    class ItemViewHolder(
        itemView: View) :
        ViewHolder(itemView) ,Consumer<Pair<String, Drawable?>>{
        lateinit var pokemon: Pokemon

        var disposable : Disposable? = null

        fun drawView(mapperContract: MapperContract) {
            val tvName = itemView.pokemon_item_name
            tvName.text = mapperContract.fromDataToUI(pokemon).name
        }

        override fun accept(map: Pair<String, Drawable?>) {
            if(map.first == pokemon.url){
                itemView.pokemon_item_image.setImageDrawable(map.second)
            }

        }

        fun subscribe(subject : PublishSubject<Pair<String, Drawable?>>){
            disposable = subject.subscribe(this)
        }

        fun unsubscribe(){
            disposable?.dispose()
        }

    }

    class ProgressViewHolder(itemView: View) : ViewHolder(itemView)
    companion object {
        const val ITEM_TYPE = 1
        const val PROGRESS_TYPE = 2
    }
}