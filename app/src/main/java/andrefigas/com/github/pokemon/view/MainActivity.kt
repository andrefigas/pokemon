package andrefigas.com.github.pokemon.view

import andrefigas.com.github.pokemon.AndroidApplication
import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.ext.doOnScrollEnding
import andrefigas.com.github.pokemon.ext.getDisplayWidth
import andrefigas.com.github.pokemon.model.entities.Pokemon
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), MainActivityContract {

    @Inject
    lateinit var pokemonListViewModel: PokemonListViewModel
    lateinit var adapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as AndroidApplication).appComponent.inject(this)

        setContentView(R.layout.activity_main)

        pokemonListViewModel.fetchData(this, this)
    }

    override fun createPokemonList() {
        adapter = PokemonAdapter()
        rv_pokemons.adapter = adapter
        configureLayoutManager(adapter)
        configureScrollListener()
    }

    private fun configureLayoutManager(adapter: PokemonAdapter) {
        val rows: Int =
            (getDisplayWidth().toDouble() / resources.getDimensionPixelSize(R.dimen.aproximated_item_width)).roundToInt()
        val layoutManager = GridLayoutManager(this, rows)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (adapter.getItemViewType(position) == PokemonAdapter.PROGRESS_TYPE) {
                    return rows;
                }

                return 1
            }

        }

        rv_pokemons.layoutManager = layoutManager
    }

    private fun configureScrollListener() {
        val offsetTriggerScroll = 3
        rv_pokemons.doOnScrollEnding(
            offsetTriggerScroll,
            {
                pokemonListViewModel.fetchData(this@MainActivity, this@MainActivity);
            },
            {
                pokemonListViewModel.isProgressing()
            }
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
}