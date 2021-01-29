package andrefigas.com.github.pokemon.injection.components

import andrefigas.com.github.pokemon.view.MainActivity
import andrefigas.com.github.pokemon.injection.modules.NetworkModule
import dagger.Component

@Component(modules = [NetworkModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

}