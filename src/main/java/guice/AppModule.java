package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import mainApp.MainApp;
import service.FxmlLoaderService;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FxmlLoaderService.class).asEagerSingleton();
    }

    @Provides
    @Named("Width")
    int provideMapWidth() { return MainApp.WIDTH; }

    @Provides
    @Named("Height")
    int provideMapHeight() { return MainApp.HEIGHT; }

    @Provides
    @Named("DalekNumber")
    int provideDalekNumber() { return MainApp.DALEK_NUMBER; }
}
