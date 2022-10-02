package andrefigas.com.github.pokemon.domain.usecases.details

import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepositoryContract
import andrefigas.com.github.pokemon.domain.usecases.BaseUseCase
import andrefigas.com.github.pokemon.domain.usecases.BaseUseCaseContract
import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.target.Target

class GetPokemonDetailsImageUseCase(private val repo: PokemonDetailsRepositoryContract)  : BaseUseCase(),
    GetPokemonDetailsImageUseCaseContract {

    override operator fun invoke(context : Context, target: Target) {
        val imageLoader = ImageLoader.Builder(context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()

        imageLoader.enqueue(repo.loadPokemonImage(target))
    }

}

interface GetPokemonDetailsImageUseCaseContract : BaseUseCaseContract {

    operator fun invoke(context: Context, target: Target)

}
