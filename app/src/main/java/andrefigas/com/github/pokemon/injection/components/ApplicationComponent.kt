package andrefigas.com.github.pokemon.injection.components

import andrefigas.com.github.pokemon.injection.modules.NetworkModule
import andrefigas.com.github.pokemon.view.details.DetailsActivity
import andrefigas.com.github.pokemon.view.main.MainActivity
import dagger.Component

@Component(modules = [NetworkModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: DetailsActivity)

}