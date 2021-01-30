package andrefigas.com.github.pokemon.view.details

import andrefigas.com.github.pokemon.AndroidApplication
import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.utils.toString
import andrefigas.com.github.pokemon.viewmodel.PokemonDetailsViewModel
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity(), DetailsActivityContract {

    @Inject
    lateinit var pokemonDetailsViewModel: PokemonDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        (applicationContext as AndroidApplication).appComponent.inject(this)

        pokemonDetailsViewModel.fetchData(this, intent, this)

        enableGoBack()

    }

    private fun enableGoBack() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun showPokemonImage(drawable: Drawable) {
        details_image.setImageDrawable(drawable)
    }

    override fun showPokemonName(name: String) {
        supportActionBar?.let{
            title = name
        }
    }

    override fun showPokemonDescription(description: String) {
        details_description.text = description
    }

    override fun showStartingDataProgress() {
        details_progress.visibility = View.VISIBLE
        //labels
        details_types_label.visibility = View.GONE
        details_habitat_label.visibility = View.GONE
        details_weight_label.visibility = View.GONE
        details_height_label.visibility = View.GONE
        details_moves_label.visibility = View.GONE
    }

    override fun hideStartingDataProgress() {
        details_progress.visibility = View.GONE
        //labels
        details_types_label.visibility = View.VISIBLE
        details_habitat_label.visibility = View.VISIBLE
        details_weight_label.visibility = View.VISIBLE
        details_height_label.visibility = View.VISIBLE
        details_moves_label.visibility = View.VISIBLE
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
            details_moves_value.ellipsize = null
            details_moves_value.maxLines = Int.MAX_VALUE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}