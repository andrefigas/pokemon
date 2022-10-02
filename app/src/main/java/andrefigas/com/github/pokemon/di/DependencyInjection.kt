package andrefigas.com.github.pokemon.di


import andrefigas.com.github.pokemon.data.repository.mappers.Mapper
import andrefigas.com.github.pokemon.data.repository.mappers.MapperContract
import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepository
import andrefigas.com.github.pokemon.data.repository.PokemonDetailsRepositoryContract
import andrefigas.com.github.pokemon.data.repository.PokemonRepository
import andrefigas.com.github.pokemon.data.repository.PokemonRepositoryContract
import andrefigas.com.github.pokemon.domain.usecases.details.*
import andrefigas.com.github.pokemon.domain.usecases.list.*
import andrefigas.com.github.pokemon.viewmodel.PokemonDetailsViewModel
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val modules = module {

    //single

    single<MapperContract> {
        Mapper(androidContext())
    }

    single<PokemonRepositoryContract> { params ->
        PokemonRepository(androidContext(), params.get())
    }

    //details

    factory<PokemonDetailsRepositoryContract> { params ->
        PokemonDetailsRepository(get(), params.get(), params.get(), get())
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