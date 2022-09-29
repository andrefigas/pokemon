package andrefigas.com.github.pokemon.tests


import andrefigas.com.github.pokemon.data.DataTest
import andrefigas.com.github.pokemon.data.MockPokemonsListRepository
import andrefigas.com.github.pokemon.view.entities.PokemonListData
import andrefigas.com.github.pokemon.viewmodel.PokemonListViewModel
import android.os.Looper
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.rxjava3.core.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.koin.core.context.GlobalContext
import org.koin.java.KoinJavaComponent.inject

class PokemonListTest : BaseUnitTests() {

    private val pokemonListViewModel: PokemonListViewModel by inject (PokemonListViewModel::class.java)

    @Test
    fun initialLoadData() {

        pokemonListViewModel.initialLoad.assert(
            { data ->
                data.pokemons.contentEquals(arrayOf(DataTest.BULBASAUR))
            },
            {
                pokemonListViewModel.fetchData()
            }
        )

    }


}