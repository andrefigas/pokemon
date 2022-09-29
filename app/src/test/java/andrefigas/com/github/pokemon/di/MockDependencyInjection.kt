package andrefigas.com.github.pokemon.di


import andrefigas.com.github.pokemon.data.MockPokemonDetailsRepository
import andrefigas.com.github.pokemon.data.MockPokemonsListRepository
import andrefigas.com.github.pokemon.data.mappers.MapperContract
import andrefigas.com.github.pokemon.data.mappers.MockMapper
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


    factory<MapperContract> {
        MockMapper
    }

    factory<PokemonRepositoryContract> {
        MockPokemonsListRepository()
    }

    factory<PokemonDetailsRepositoryContract> { params ->
        MockPokemonDetailsRepository(params.get(), get())
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