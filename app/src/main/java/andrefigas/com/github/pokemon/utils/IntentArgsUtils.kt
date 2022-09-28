package andrefigas.com.github.pokemon.utils

import andrefigas.com.github.pokemon.domain.entities.Pokemon
import android.content.Intent

object IntentArgsUtils {

    private const val IK_POKEMON = "pokemon"

    fun putPokemonInArgs(intent: Intent, pokemon: Pokemon?) = intent.putExtra(IK_POKEMON, pokemon)

    fun getPokemonByArgs(intent: Intent) = intent.getParcelableExtra<Pokemon>(IK_POKEMON) as Pokemon
}