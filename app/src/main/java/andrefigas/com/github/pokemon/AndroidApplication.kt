package andrefigas.com.github.pokemon

import andrefigas.com.github.pokemon.di.modules
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class AndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@AndroidApplication)
            modules(listOf(modules))
        }
    }
}