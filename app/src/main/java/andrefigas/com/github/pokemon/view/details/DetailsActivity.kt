package andrefigas.com.github.pokemon.view.details

import andrefigas.com.github.pokemon.AndroidApplication
import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.utils.toString
import andrefigas.com.github.pokemon.viewmodel.PokemonDetailsViewModel
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject


class DetailsActivity : AppCompatActivity(), DetailsActivityContract {

    @Inject
    lateinit var pokemonDetailsViewModel: PokemonDetailsViewModel
    lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        (applicationContext as AndroidApplication).appComponent.inject(this)

        enableGoBack()


    }

    override fun onResume() {
        super.onResume()


        pokemonDetailsViewModel.fetchData(this, intent, this)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * enable actionbar's arrow back
     */
    private fun enableGoBack() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun showPokemonImage(drawable: Drawable) {
        details_image.setImageDrawable(drawable)
    }

    override fun showPokemonErrorImage(drawable: Drawable) {
        details_image.setImageDrawable(drawable)
    }

    override fun showPokemonName(name: String) {
        supportActionBar?.let {
            title = name
        }
    }

    override fun showPokemonDescription(description: String) {
        details_description.text = description
    }

    override fun showStartingDataProgress() {
        details_progress.visibility = View.VISIBLE
    }

    override fun hideStartingDataProgress() {
        details_progress.visibility = View.GONE
    }

    override fun hideAllFields() {
        //labels
        details_types_label.visibility = View.GONE
        details_habitat_label.visibility = View.GONE
        details_weight_label.visibility = View.GONE
        details_height_label.visibility = View.GONE
        details_moves_label.visibility = View.GONE
        //values
        details_types_values.visibility = View.GONE
        details_habitat_value.visibility = View.GONE
        details_weight_value.visibility = View.GONE
        details_height_value.visibility = View.GONE
        details_moves_value.visibility = View.GONE
        details_description.visibility = View.GONE
    }

    override fun showAllFields() {
        //labels
        details_types_label.visibility = View.VISIBLE
        details_habitat_label.visibility = View.VISIBLE
        details_weight_label.visibility = View.VISIBLE
        details_height_label.visibility = View.VISIBLE
        details_moves_label.visibility = View.VISIBLE
        //values
        details_types_values.visibility = View.VISIBLE
        details_habitat_value.visibility = View.VISIBLE
        details_weight_value.visibility = View.VISIBLE
        details_height_value.visibility = View.VISIBLE
        details_moves_value.visibility = View.VISIBLE
        details_description.visibility = View.VISIBLE
    }

    override fun showPreloadedFields() {
        //labels
        details_types_label.visibility = View.VISIBLE
        details_weight_label.visibility = View.VISIBLE
        details_height_label.visibility = View.VISIBLE
        details_moves_label.visibility = View.VISIBLE
        //values
        details_types_values.visibility = View.VISIBLE
        details_weight_value.visibility = View.VISIBLE
        details_height_value.visibility = View.VISIBLE
        details_moves_value.visibility = View.VISIBLE
    }

    override fun showTypes(types: List<String>) {
        details_types_values.text = types.toString(getString(R.string.types_separator))
    }

    override fun showHabitat(habitat: String) {
        details_habitat_value.text = habitat
    }

    override fun showPokemonWeight(weight: Int) {
        details_weight_value.text = weight.toString()
    }

    override fun showPokemonHeight(height: Int) {
        details_height_value.text = height.toString()
    }

    override fun showPokemonMoves(moves: List<String>) {
        details_moves_value.text = moves.toString(getString(R.string.types_separator))
        details_moves_value.setOnClickListener {
            //expand shorted text
            details_moves_value.ellipsize = null
            details_moves_value.maxLines = Int.MAX_VALUE
        }
    }

    private fun addToFavorite(item: MenuItem): Boolean {
        //retains interactions while progress
        if (pokemonDetailsViewModel.isUpdateProgressing()) {
            return false
        }

        item.isVisible = false
        menu.findItem(R.id.action_favorite_check).isVisible = true
        pokemonDetailsViewModel.updateFavorite(this, false)
        return true
    }

    private fun removeFromFavorite(item: MenuItem): Boolean {
        //retains interactions while progress
        if (pokemonDetailsViewModel.isUpdateProgressing()) {
            return false
        }

        item.isVisible = false
        menu.findItem(R.id.action_favorite_uncheck).isVisible = true
        pokemonDetailsViewModel.updateFavorite(this, true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            R.id.action_favorite_uncheck -> addToFavorite(item)
            R.id.action_favorite_check -> removeFromFavorite(item)
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun showFavoriteChecked() {
        menu.findItem(R.id.action_favorite_uncheck).isVisible = true
        menu.findItem(R.id.action_favorite_check).isVisible = false
    }

    override fun showFavoriteUnchecked() {
        menu.findItem(R.id.action_favorite_uncheck).isVisible = false
        menu.findItem(R.id.action_favorite_check).isVisible = true
    }

    override fun showAddFavoriteUpdateSuccess(name: String) {
        Toast.makeText(this, getString(R.string.add_favorite_message, name), Toast.LENGTH_LONG)
            .show()
    }

    override fun showRemoveFavoriteUpdateSuccess(name: String) {
        Toast.makeText(this, getString(R.string.remove_favorite_message, name), Toast.LENGTH_LONG)
            .show()
    }

    override fun toggleFavoriteCheck() {
        menu.findItem(R.id.action_favorite_check).isVisible =
            !menu.findItem(R.id.action_favorite_check).isVisible
        menu.findItem(R.id.action_favorite_uncheck).isVisible =
            !menu.findItem(R.id.action_favorite_uncheck).isVisible
    }

    override fun showErrorOnAddFavorite(name: String) {
        Toast.makeText(
            this,
            getString(R.string.pokemon_add_to_favorite_error, name),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun showErrorOnRemoveFavorite(name: String) {
        Toast.makeText(
            this,
            getString(R.string.pokemon_remove_from_favorite_error, name),
            Toast.LENGTH_LONG
        ).show()
    }
}