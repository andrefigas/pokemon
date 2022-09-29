package andrefigas.com.github.pokemon.view.details

import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.view.entities.PokemonDetailsData
import andrefigas.com.github.pokemon.utils.IntentArgsUtils
import andrefigas.com.github.pokemon.utils.toString
import andrefigas.com.github.pokemon.view.entities.PokemonUI
import andrefigas.com.github.pokemon.viewmodel.PokemonDetailsViewModel
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil.request.ImageRequest
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class DetailsActivity : AppCompatActivity(){

    private val pokemonDetailsViewModel : PokemonDetailsViewModel by viewModel{
        parametersOf(
            IntentArgsUtils.getPokemonByArgs(intent),
            ImageRequest.Builder(DetailsActivity@this)
                .crossfade(true)
                .crossfade(500)
                .placeholder(R.drawable.ic_pokeball_pb)
                .error(R.drawable.ic_pokeball)
        )
    }

    private lateinit var menu: Menu

    private fun getPokemonAttachedInIntent() = pokemonDetailsViewModel.mapperContract.fromDataToUI(IntentArgsUtils.getPokemonByArgs(intent))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        enableGoBack()

        showPreloadedInfo(getPokemonAttachedInIntent())
    }

    private fun onViewCreated(){
        showStartingDataProgress()
        observeChanges()
        pokemonDetailsViewModel.fetchData()
        pokemonDetailsViewModel.fetchImage(this)
    }

    private fun observeChanges(){
        observeDetailsSuccess()
        observeDetailsError()
        observeImage()
        observeUpdateFavorite()
        observeUpdateFavoriteError()
    }

    private fun observeDetailsSuccess(){
        pokemonDetailsViewModel.details.observe(this,  Observer<PokemonDetailsData.DetailsSuccess> { success ->
            hideStartingDataProgress()

            val data = pokemonDetailsViewModel.mapperContract.fromDataToUI(success.data)

            showPokemonDescription(data.description)
            showHabitat(data.habitat)
            showAllFields()

            if (data.favorite) {
                showFavoriteChecked()
            } else {
                showFavoriteUnchecked()
            }
        })
    }

    private fun observeDetailsError(){
        pokemonDetailsViewModel.detailsError.observe(this, Observer<PokemonDetailsData.DetailsError>{ error ->
            showPreloadedFields()
        })
    }

    private fun observeImage(){
        pokemonDetailsViewModel.image.observe(this, Observer<PokemonDetailsData.LoadImage> { success ->
            showPokemonImage(success.data)
        })
    }

    private fun observeUpdateFavorite(){
        pokemonDetailsViewModel.updateFavorite.observe(this, Observer<PokemonDetailsData.UpdateSuccess> { success ->
            showFavoriteStatus(success.checked)
        })
    }

    private fun observeUpdateFavoriteError(){
        pokemonDetailsViewModel.updateFavoriteError.observe(this, Observer<PokemonDetailsData.UpdateError> { checkError ->
            showFavoriteUpdateError(checkError.checked)
        })
    }


    private fun showPreloadedInfo(pokemon : PokemonUI) {
        with(pokemon){
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

    private fun showPokemonImage(drawable: Drawable) {
        details_image.setImageDrawable(drawable)
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

    private fun showTypes(types: List<String>) {
        details_types_values.text = types.toString(getString(R.string.types_separator))//todo fix
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
        pokemonDetailsViewModel.updateFavourite(false)
        return true
    }

    private fun removeFromFavorite(item: MenuItem): Boolean {
        item.isVisible = false
        menu.findItem(R.id.action_favorite_uncheck).isVisible = true
        pokemonDetailsViewModel.updateFavourite(true)
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

    private fun showFavoriteStatus(checked : Boolean){
        if(checked){
            showFavoriteChecked()
        }else{
            showFavoriteUnchecked()
        }
    }

    private fun showFavoriteUpdateError(checked : Boolean){
        val pokemonName = IntentArgsUtils.getPokemonByArgs(intent).name
        if(checked){
            showErrorOnAddFavorite(pokemonName)
        }else{
            showRemoveFavoriteUpdateSuccess(pokemonName)
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

    private fun showAddFavoriteUpdateSuccess(name: String) {
        Toast.makeText(this, getString(R.string.add_favorite_message, name), Toast.LENGTH_LONG)
            .show()
    }

    private fun showRemoveFavoriteUpdateSuccess(name: String) {
        Toast.makeText(this, getString(R.string.remove_favorite_message, name), Toast.LENGTH_LONG)
            .show()
    }

    private fun toggleFavoriteCheck() {
        menu.findItem(R.id.action_favorite_check).isVisible =
            !menu.findItem(R.id.action_favorite_check).isVisible
        menu.findItem(R.id.action_favorite_uncheck).isVisible =
            !menu.findItem(R.id.action_favorite_uncheck).isVisible
    }

    private fun showErrorOnAddFavorite(name: String) {
        Toast.makeText(
            this,
            getString(R.string.pokemon_add_to_favorite_error, name),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showErrorOnRemoveFavorite(name: String) {
        Toast.makeText(
            this,
            getString(R.string.pokemon_remove_from_favorite_error, name),
            Toast.LENGTH_LONG
        ).show()
    }
}