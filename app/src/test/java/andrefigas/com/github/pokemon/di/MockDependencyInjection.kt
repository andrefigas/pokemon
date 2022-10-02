package andrefigas.com.github.pokemon.di


import andrefigas.com.github.pokemon.data.repository.*
import andrefigas.com.github.pokemon.data.repository.mappers.MockMapper
import andrefigas.com.github.pokemon.domain.usecases.details.GetPokemonDetailsUseCase
import andrefigas.com.github.pokemon.domain.usecases.details.GetPokemonDetailsUseCaseContract
import andrefigas.com.github.pokemon.domain.usecases.details.GetPokemonDetailsImageUseCase
import andrefigas.com.github.pokemon.domain.usecases.details.GetPokemonDetailsImageUseCaseContract
import andrefigas.com.github.pokemon.domain.usecases.details.UpdatePokemonFavoriteUseCase
import andrefigas.com.github.pokemon.domain.usecases.details.UpdatePokemonFavoriteUseCaseContract
import andrefigas.com.github.pokemon.domain.usecases.list.*
import andrefigas.com.github.pokemon.viewmodel.PokemonDetailsViewModel
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val modules = module {

    //single

    single {
        MockMapper
    }

    single<PokemonRepositoryContract> {
        MockPokemonsListRepository()
    }

    //details

    factory<PokemonDetailsRepositoryContract> { params ->
        MockPokemonDetailsRepository(params.get(), get())
    }

    factory<GetPokemonDetailsUseCaseContract>{ params ->
        GetPokemonDetailsUseCase(get {
            params
        })
    }

    factory<GetPokemonDetailsImageUseCaseContract>{ params ->
        GetPokemonDetailsImageUseCase(get {
            params
        })
    }

    factory<UpdatePokemonFavoriteUseCaseContract> { params ->
        UpdatePokemonFavoriteUseCase(get {
            params
        })
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


    //list

    factory<GetchInitialPokemonsPageUseCaseContract> { params ->
        GetInitialPokemonsPageUseCase(get {
            params
        })
    }

    factory<GetNextPokemonsPageUseCaseContract> { params ->
        GetNextPokemonsPageUseCase(get {
            params
        })
    }

    factory<GetPokemonListImageUseCaseContract> { params ->
        GetPokemonListImageUseCase(get {
            params
        })
    }

    viewModel { params ->
        PokemonListViewModel(
            get {
                params
            },
            get {
                params
            },
            get {
                params
            },
            get())

    }


}