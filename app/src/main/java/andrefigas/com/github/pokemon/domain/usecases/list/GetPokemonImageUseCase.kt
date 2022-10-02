package andrefigas.com.github.pokemon.domain.usecases.list

import andrefigas.com.github.pokemon.data.repository.PokemonRepositoryContract
import andrefigas.com.github.pokemon.domain.entities.Pokemon
import andrefigas.com.github.pokemon.domain.usecases.BaseUseCase
import andrefigas.com.github.pokemon.domain.usecases.BaseUseCaseContract
import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.target.Target

class GetPokemonListImageUseCase(private val repo: PokemonRepositoryContract) : BaseUseCase(),
    GetPokemonListImageUseCaseContract {

    override operator fun invoke(context : Context, pokemon: Pokemon, target: Target) {
        val imageLoader = ImageLoader.Builder(context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()

        imageLoader.enqueue(repo.loadPokemonImage(pokemon, target))
    }


}

interface GetPokemonListImageUseCaseContract : BaseUseCaseContract {

    operator fun invoke(context: Context, pokemon: Pokemon, target: Target)
}
