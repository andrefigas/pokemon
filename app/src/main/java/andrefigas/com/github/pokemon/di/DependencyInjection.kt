package andrefigas.com.github.pokemon.di


import andrefigas.com.github.pokemon.data.mappers.Mapper
import andrefigas.com.github.pokemon.data.mappers.MapperContract
import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepository
import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepositoryContract
import andrefigas.com.github.pokemon.data.repository.PokemonRepository
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
        PokemonRepository(androidContext(), params.get(), get())
    }

    factory<PokemonDetailsRepositoryContract> { params ->
        PokemonDetailsRepository(get(), params.get(), params.get(), get())
    }

    factory<GetPokemonImageUseCase> { params ->
        GetPokemonImageUseCase(get {
            params
        })
    }

    factory<UpdatePokemonFavoriteUseCase> { params ->
        UpdatePokemonFavoriteUseCase(get {
            params
        })
    }

    factory<GetPokemonDetailsUseCase> { params ->
        GetPokemonDetailsUseCase(get {
            params
        })
    }

    factory<GetPokemonsUseCase> { params ->
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