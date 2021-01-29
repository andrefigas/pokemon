package andrefigas.com.github.pokemon.injection.modules;

import andrefigas.com.github.pokemon.AndroidApplication;
import dagger.Module;

@Module
public class ApplicationModule {

    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }
}
