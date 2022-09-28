package andrefigas.com.github.pokemon.domain.usecases

import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepositoryContract
import coil.request.ImageRequest
import coil.target.Target
import com.squareup.okhttp.ResponseBody
import io.reactivex.Single

class GetPokemonDetailsUseCase(private val repo: PokemonDetailsRepositoryContract) {

    fun provideDetails() = repo.provideDetails()

}
