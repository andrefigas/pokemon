package andrefigas.com.github.pokemon.tests

import andrefigas.com.github.pokemon.MockApiClient
import androidx.annotation.CallSuper
import org.junit.After
import org.junit.Before
import org.mockito.MockitoAnnotations

open class BaseUnitTests {

    lateinit var networkModule: MockApiClient

    @After
    fun tearDown() {
        networkModule.webServer.close()
    }

    @CallSuper
    @Before
    open fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

}