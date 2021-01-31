package andrefigas.com.github.pokemon.view.main

import andrefigas.com.github.pokemon.AndroidApplication
import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.utils.doOnScrollEnding
import andrefigas.com.github.pokemon.utils.getDisplayWidth
import andrefigas.com.github.pokemon.model.entities.Pokemon
import andrefigas.com.github.pokemon.utils.IntentArgsUtils
import andrefigas.com.github.pokemon.view.details.DetailsActivity
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(),
    MainActivityContract {

    @Inject
    lateinit var pokemonListViewModel: PokemonListViewModel
    lateinit var adapter: PokemonAdapter
    var infinityScrollListener : RecyclerView.OnScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as AndroidApplication).appComponent.inject(this)

        setContentView(R.layout.activity_main)

        pokemonListViewModel.fetchData(this, this)
    }

    override fun onResume() {
        super.onResume()
        configureInfinityScroll()
    }

    override fun createPokemonList() {
        adapter = PokemonAdapter(this)
        rv_pokemons.adapter = adapter
        configureLayoutManager(adapter)
        configureInfinityScroll()
    }

    private fun configureLayoutManager(adapter: PokemonAdapter) {
        val rows: Int =
            (getDisplayWidth().toDouble() / resources.getDimensionPixelSize(R.dimen.aproximated_item_width)).roundToInt()
        val layoutManager = GridLayoutManager(this, rows)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (adapter.getItemViewType(position) == PokemonAdapter.PROGRESS_TYPE) {
                    return rows
                }

                return 1
            }

        }

        rv_pokemons.layoutManager = layoutManager
    }

    private fun configureInfinityScroll() {
        val offsetTriggerScroll = 3
        infinityScrollListener = rv_pokemons.doOnScrollEnding(
            offsetTriggerScroll,
            {
                pokemonListViewModel.fetchData(this@MainActivity, this@MainActivity)
            },
            {
                pokemonListViewModel.isProgressing()
            }
        )
    }

    override fun navigateToDetails(pokemon: Pokemon, target: View ) {
        val options: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                target,
                getString(R.string.image_key)
            )

        startActivity(
            IntentArgsUtils.putPokemonInArgs(
                Intent(
                    this,
                    DetailsActivity::class.java
                ), pokemon
            ), options.toBundle()
        )
    }

    override fun increasePokemonList(pokemons: List<Pokemon>) {
        adapter.addPokemons(pokemons)
    }

    override fun showIncreasingDataProgress() {
        adapter.setProgressing(true)
    }

    override fun hideIncreasingDataProgress() {
        adapter.setProgressing(false)
    }

    override fun showStartingDataProgress() {
        pb_initial.visibility = View.VISIBLE
    }

    override fun hideStartingDataProgress() {
        pb_initial.visibility = View.GONE
    }

    override fun showInitialLoadDataError() {
        initial_load_error_message.visibility = View.VISIBLE
        initial_load_error_message.setOnClickListener {
            pokemonListViewModel.fetchData(this, this)
        }
    }

    override fun hideInitialLoadDataError() {
        initial_load_error_message.visibility = View.GONE
    }

    override fun showIncreasingDataDataError() {
        Toast.makeText(this, getString(R.string.pokemon_list_increase_data_error), Toast.LENGTH_LONG).show()
    }

    override fun disableInfinityScroll() {
        infinityScrollListener?.let {
            rv_pokemons.removeOnScrollListener(it)
        }

        infinityScrollListener = null

    }
}