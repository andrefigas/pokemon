package andrefigas.com.github.pokemon

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MockApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@MockApplication)
            modules(listOf(andrefigas.com.github.pokemon.di.modules))
        }
    }
}