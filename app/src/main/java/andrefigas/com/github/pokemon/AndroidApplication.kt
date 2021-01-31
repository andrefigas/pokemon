package andrefigas.com.github.pokemon

import andrefigas.com.github.pokemon.injection.components.DaggerApplicationComponent
import android.app.Application

class AndroidApplication : Application() {
    var appComponent = DaggerApplicationComponent.create()
}