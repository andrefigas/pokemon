package andrefigas.com.github.pokemon;

import android.app.Application;

import andrefigas.com.github.pokemon.injection.components.ApplicationComponent;
import andrefigas.com.github.pokemon.injection.components.DaggerApplicationComponent;

public class AndroidApplication  extends Application {

    public ApplicationComponent appComponent = DaggerApplicationComponent.create();

}
