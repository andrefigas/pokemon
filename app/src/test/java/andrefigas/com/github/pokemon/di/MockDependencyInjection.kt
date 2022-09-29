package andrefigas.com.github.pokemon.di


import andrefigas.com.github.pokemon.data.repository.MockPokemonDetailsRepository
import andrefigas.com.github.pokemon.data.repository.MockPokemonsListRepository
import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.data.repository.mappers.MockMapper
import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepositoryContract
import andrefigas.com.github.pokemon.data.repository.PokemonRepositoryContract
import andrefigas.com.github.pokemon.domain.usecases.GetPokemonDetailsUseCase
import andrefigas.com.github.pokemon.domain.usecases.GetPokemonImageUseCase
import andrefigas.com.github.pokemon.domain.usecases.GetPokemonsUseCase
import andrefigas.com.github.pokemon.domain.usecases.UpdatePokemonFavoriteUseCase
import andrefigas.com.github.pokemon.viewmodel.PokemonDetailsViewModel
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import org.koin.dsl.module

val modules = module {


    factory {
        MockMapper
    }

    factory<PokemonRepositoryContract> {
        MockPokemonsListRepository()
    }

    factory<PokemonDetailsRepositoryContract> { params ->
        MockPokemonDetailsRepository(params.get(), get())
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

    single { params ->
        PokemonListViewModel(
            get {
                params
            },
            get())

    }

    single { params ->
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