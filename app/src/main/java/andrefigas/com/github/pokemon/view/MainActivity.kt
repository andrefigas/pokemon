package andrefigas.com.github.pokemon.view

import andrefigas.com.github.pokemon.AndroidApplication
import andrefigas.com.github.pokemon.R
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var pokemonListViewModel: PokemonListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as AndroidApplication).appComponent.inject(this)

        setContentView(R.layout.activity_main)

        pokemonListViewModel.init(this, Observer { pokemonList ->
            System.out.println(pokemonList)
        })
    }

}