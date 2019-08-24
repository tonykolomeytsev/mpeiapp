package kekmech.ru.mpeiapp;

import android.app.Application;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.Router;

import javax.inject.Inject;


public class MPEIApp extends Application implements HasAndroidInjector {
    @Inject
    DispatchingAndroidInjector<Object> dispatchingAndroidInjector;
    private Cicerone<Router> cicerone;

    @Override
    public void onCreate() {
        super.onCreate();
        cicerone = Cicerone.create();
        DaggerAppComponent.Companion
                .init(this, cicerone)
                .inject(this);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return dispatchingAndroidInjector;
    }
}

