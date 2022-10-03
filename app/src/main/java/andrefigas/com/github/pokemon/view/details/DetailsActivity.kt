package andrefigas.com.github.pokemon.view.details

import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.data.entities.PokemonDetailsDataModel
import andrefigas.com.github.pokemon.intent.ImagePageState
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageEffect
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageEvent
import andrefigas.com.github.pokemon.intent.details.PokemonDetailsPageState
import andrefigas.com.github.pokemon.utils.IntentArgsUtils
import andrefigas.com.github.pokemon.utils.observe
import andrefigas.com.github.pokemon.view.entities.PokemonUI
import andrefigas.com.github.pokemon.viewmodel.PokemonDetailsViewModel
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil.request.ImageRequest
import io.reactivex.rxjava3.functions.Consumer
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class DetailsActivity : AppCompatActivity() {

    private val pokemonDetailsViewModel: PokemonDetailsViewModel by viewModel {
        parametersOf(
            IntentArgsUtils.getPokemonByArgs(intent),
            ImageRequest.Builder(applicationContext)
                .crossfade(true)
                .crossfade(500)
                .placeholder(R.drawable.ic_pokeball_pb)
                .error(R.drawable.ic_pokeball)
        )
    }

    private lateinit var menu: Menu

    private fun getPokemonAttachedInIntent() =
        pokemonDetailsViewModel.mapperContract.fromDataToUI(IntentArgsUtils.getPokemonByArgs(intent))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        enableGoBack()
    }

    private fun onViewCreated() {
        showStartingDataProgress()
        observeChanges()

        pokemonDetailsViewModel.processEvent(PokemonDetailsPageEvent.OnCreate)
        pokemonDetailsViewModel.processEvent(PokemonDetailsPageEvent.OnRequestImage(this))
    }

    private fun observeChanges() {

        pokemonDetailsViewModel.pageState.observe(this, Observer<PokemonDetailsPageState> { state ->
            renderState(state)
        })

        pokemonDetailsViewModel.imageState.observe(this, Observer<ImagePageState> { state ->
            renderState(state)
        })

        pokemonDetailsViewModel.effects.observe(this, Consumer{ effect ->
            showEffect(effect)
        })

    }

    private fun showEffect(effect: PokemonDetailsPageEffect) {
        when(effect){
            is PokemonDetailsPageEffect.OnAddToFavoriteFail -> showAddFavoriteUpdateFail(effect)
            is PokemonDetailsPageEffect.OnAddToFavoriteSuccess -> showAddFavoriteUpdateSuccess(effect)
            is PokemonDetailsPageEffect.OnRemoveFromFavoriteFail -> showRemoveFavoriteUpdateSuccess(effect)
            is PokemonDetailsPageEffect.OnRemoveFromFavoriteSuccess -> showRemoveFavoriteUpdateFail(effect)
        }
    }


    private fun renderState(state: ImagePageState) {
        when (state) {
            is ImagePageState.Recycled,
            is ImagePageState.OnSuccess -> {
                showPokemonImage(state)
            }
            is ImagePageState.OnFail -> {
                showPokemonImageFail(state)
            }

        }
    }

    private fun renderState(state: PokemonDetailsPageState) {
        when (state) {
            is PokemonDetailsPageState.Loading -> {
                showStartingDataProgress()
            }

            is PokemonDetailsPageState.Recycled,
            is PokemonDetailsPageState.UpdateFavoriteInSuccess,
            is PokemonDetailsPageState.DetailsSuccess -> {
                showDetails(state)
            }

            is PokemonDetailsPageState.DetailsFail -> {
                showDetailsError()
            }
        }
    }


    private fun showDetails(state: PokemonDetailsPageState) {
        hideStartingDataProgress()
        showPreloadedInfo(getPokemonAttachedInIntent())

        showAllFields()

        val data = pokemonDetailsViewModel.mapperContract.fromDataToUI(state.data as PokemonDetailsDataModel)
        showPokemonDescription(data.description)
        showHabitat(data.habitat)
        showAllFields()

        if (data.favorite) {
            showFavoriteChecked()
        } else {
            showFavoriteUnchecked()
        }
    }

    private fun showDetailsError() {
        hideStartingDataProgress()
        showPreloadedFields()
        showPreloadedInfo(getPokemonAttachedInIntent())
    }

    private fun showPreloadedInfo(pokemon: PokemonUI) {
        with(pokemon) {
            showTypes(types)

            showPokemonName(name)

            showPokemonWeight(weight)

            showPokemonHeight(height)

            showPokemonMoves(moves)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_details, menu)

        onViewCreated()

        return super.onCreateOptionsMenu(menu)
    }

    private fun enableGoBack() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun showPokemonImage(state: ImagePageState) {
        details_image.setImageDrawable(state.image)
    }

    private fun showPokemonImageFail(state: ImagePageState) {
        details_image.setImageDrawable(state.error)
    }

    private fun showPokemonName(name: String) {
        supportActionBar?.let {
            title = name
        }
    }

    private fun showPokemonDescription(description: String) {
        details_description.text = description
    }

    private fun showStartingDataProgress() {
        details_progress.visibility = View.VISIBLE
    }

    private fun hideStartingDataProgress() {
        details_progress.visibility = View.GONE
    }

    private fun showAllFields() {
        showPreloadedFields()

        listOf(
            //labels
            details_habitat_label,

            //values
            details_habitat_value,
            details_description
        ).forEach {
            it.visibility = View.VISIBLE
        }

    }

    private fun showPreloadedFields() {
        listOf(
            //labels
            details_types_label,
            details_weight_label,
            details_height_label,
            details_moves_label,
            //values
            details_types_values,
            details_weight_value,
            details_height_value,
            details_moves_value
        ).forEach {
            it.visibility = View.VISIBLE
        }
    }

    private fun showTypes(types: String) {
        details_types_values.text = types
    }

    private fun showHabitat(habitat: String) {
        details_habitat_value.text = habitat
    }

    private fun showPokemonWeight(weight: Int) {
        details_weight_value.text = weight.toString()
    }

    private fun showPokemonHeight(height: Int) {
        details_height_value.text = height.toString()
    }

    private fun showPokemonMoves(moves: String) {
        details_moves_value.text = moves
        details_moves_value.setOnClickListener {
            //expand shorted text
            details_moves_value.ellipsize = null
            details_moves_value.maxLines = Int.MAX_VALUE
        }
    }

    private fun addToFavorite(item: MenuItem): Boolean {
        item.isVisible = false
        menu.findItem(R.id.action_favorite_check).isVisible = true
        pokemonDetailsViewModel.processEvent(PokemonDetailsPageEvent.OnAddToFavorites)
        return true
    }

    private fun removeFromFavorite(item: MenuItem): Boolean {
        item.isVisible = false
        menu.findItem(R.id.action_favorite_uncheck).isVisible = true
        pokemonDetailsViewModel.processEvent(PokemonDetailsPageEvent.OnRemoveFromFavorites)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            R.id.action_favorite_uncheck -> removeFromFavorite(item)
            R.id.action_favorite_check -> addToFavorite(item)
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun showFavoriteChecked() {
        menu.findItem(R.id.action_favorite_uncheck).isVisible = true
        menu.findItem(R.id.action_favorite_check).isVisible = false
    }

    private fun showFavoriteUnchecked() {
        menu.findItem(R.id.action_favorite_uncheck).isVisible = false
        menu.findItem(R.id.action_favorite_check).isVisible = true
    }

    private fun showAddFavoriteUpdateSuccess(effect: PokemonDetailsPageEffect) {
        showFavoriteFeedback(R.string.add_favorite_message, effect)
    }

    private fun showRemoveFavoriteUpdateSuccess(effect: PokemonDetailsPageEffect) {
        showFavoriteFeedback(R.string.remove_favorite_message, effect)
    }

    private fun showAddFavoriteUpdateFail(effect: PokemonDetailsPageEffect) {
        showFavoriteFeedback(R.string.add_favorite_error, effect)
    }

    private fun showRemoveFavoriteUpdateFail(effect: PokemonDetailsPageEffect) {
        showFavoriteFeedback(R.string.remove_favorite_error, effect)
    }

    private fun showFavoriteFeedback(@StringRes strRes : Int, effect: PokemonDetailsPageEffect){
        Toast.makeText(this, getString(strRes, effect.name).capitalize(), Toast.LENGTH_LONG)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        pokemonDetailsViewModel.release()
    }
}