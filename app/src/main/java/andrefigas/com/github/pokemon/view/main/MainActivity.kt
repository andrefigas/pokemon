package andrefigas.com.github.pokemon.view.main

import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.intent.list.PokemonListPageEvent
import andrefigas.com.github.pokemon.intent.list.PokemonListPageState
import andrefigas.com.github.pokemon.utils.IntentArgsUtils
import andrefigas.com.github.pokemon.utils.doOnScrollEnding
import andrefigas.com.github.pokemon.utils.getDisplayWidth
import andrefigas.com.github.pokemon.view.details.DetailsActivity
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.request.ImageRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(),
    MainActivityContract {

    private val pokemonListViewModel: PokemonListViewModel by viewModel {
        parametersOf(
            ImageRequest.Builder(applicationContext)
                .crossfade(true)
                .crossfade(500)
                .placeholder(R.drawable.ic_pokeball_pb)
                .error(R.drawable.ic_pokeball)
        )
    }

    private lateinit var adapter: PokemonAdapter
    private var infinityScrollListener: RecyclerView.OnScrollListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        observeChanges()

        pokemonListViewModel.processEvent(PokemonListPageEvent.OnCreate)
    }

    private fun observeChanges(){
        pokemonListViewModel.pageState.observe(this, Observer<PokemonListPageState>{ intent ->
            renderState(intent)
        })
    }

    private fun renderState(intent: PokemonListPageState) {
        when(intent){

            is PokemonListPageState.InitialLoading->{
                showStartingDataProgress()
            }

            is PokemonListPageState.IncrementalLoading->{
                showIncreasingDataProgress()
            }

            is PokemonListPageState.InitialSuccess -> {
                initialLoadSuccess(intent)
            }

            is PokemonListPageState.IncrementalSuccess -> {
                incrementalLoadSuccess(intent)
            }

            is PokemonListPageState.InitialFail -> {
                initialLoadFail()
            }

            is PokemonListPageState.IncrementalFail -> {
                initialLoadFail()
            }
        }
    }


    private fun initialLoadSuccess(state: PokemonListPageState.InitialSuccess){
        createPokemonList()
        hideStartingDataProgress()
        increasePokemonList(state.pokemons)
        configureInfinityScroll()
    }

    private fun incrementalLoadSuccess(state: PokemonListPageState.IncrementalSuccess){
        hideIncreasingDataProgress()
        increasePokemonList(state.pokemons)
        configureInfinityScroll()
    }

    private fun initialLoadFail(){
        hideStartingDataProgress()
        disableInfinityScroll()
    }

    override fun createPokemonList() {
        adapter = PokemonAdapter(pokemonListViewModel) { view ->
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                view,
                getString(R.string.image_key)
            )
        }

        rv_pokemons.adapter = adapter
        configureLayoutManager(adapter)
    }

    private fun configureLayoutManager(adapter: PokemonAdapter) {
        val rows: Int =
            (getDisplayWidth().toDouble() / resources.getDimensionPixelSize(R.dimen.aproximated_item_width)).roundToInt()
        val layoutManager = GridLayoutManager(this, rows)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (adapter.getItemViewType(position) == PokemonAdapter.PROGRESS_TYPE) {
                    //progress item must fills display width
                    return rows
                }

                return 1
            }

        }

        rv_pokemons.layoutManager = layoutManager
    }

    private fun configureInfinityScroll() {
        val offsetTriggerScroll = 3 //refresh when in the last 3 items
        infinityScrollListener = rv_pokemons.doOnScrollEnding(
            offsetTriggerScroll,
            {
                disableInfinityScroll()
                pokemonListViewModel.processEvent(PokemonListPageEvent.OnScrollEnd)
            },
            {
                pokemonListViewModel.isLoading()
            }
        )
    }

    override fun navigateToDetails(pokemon: Pokemon, target: View) {
        //animation
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

    override fun increasePokemonList(pokemons: Array<Pokemon>) {
        adapter.addPokemons(pokemons.toList())
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
            pokemonListViewModel.processEvent(PokemonListPageEvent.OnRetry)
        }
    }

    override fun hideInitialLoadDataError() {
        initial_load_error_message.visibility = View.GONE
    }

    override fun showIncreasingDataDataError() {
        Toast.makeText(
            this,
            getString(R.string.pokemon_list_increase_data_error),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun disableInfinityScroll() {
        infinityScrollListener?.let {
            rv_pokemons.removeOnScrollListener(it)
        }

        infinityScrollListener = null

    }
}