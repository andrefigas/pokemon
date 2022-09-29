package andrefigas.com.github.pokemon.di


import andrefigas.com.github.pokemon.data.repository.AndroidMockPokemonDetailsRepository
import andrefigas.com.github.pokemon.data.repository.AndroidMockPokemonsListRepository
import andrefigas.com.github.pokemon.data.repository.mappers.Mapper
import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepositoryContract
import andrefigas.com.github.pokemon.data.repository.PokemonRepositoryContract
import andrefigas.com.github.pokemon.domain.usecases.GetPokemonDetailsUseCase
import andrefigas.com.github.pokemon.domain.usecases.GetPokemonImageUseCase
import andrefigas.com.github.pokemon.domain.usecases.GetPokemonsUseCase
import andrefigas.com.github.pokemon.domain.usecases.UpdatePokemonFavoriteUseCase
import andrefigas.com.github.pokemon.viewmodel.PokemonDetailsViewModel
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val modules = module {


    factory<MapperContract> {
        Mapper(androidContext())
    }

    factory<PokemonRepositoryContract> { params ->
        AndroidMockPokemonsListRepository(params.get())
    }

    factory<PokemonDetailsRepositoryContract> { params ->
        AndroidMockPokemonDetailsRepository(params.get(), get(), params.get())
    }

    factory { params ->
        GetPokemonImageUseCase(get {
            params
        })
    }

    factory { params ->
        UpdatePokemonFavoriteUseCase(get {
            params
        })
    }

    factory { params ->
        GetPokemonDetailsUseCase(get {
            params
        })
    }

    factory { params ->
        GetPokemonsUseCase(get {
            params
        })
    }

    viewModel { params ->
        PokemonListViewModel(
            get {
                params
            },
            get())

    }

    viewModel { params ->
        PokemonDetailsViewModel(
            get {
                params
            }, get {
                params
            }, get {
                params
            }, get())
    }

}