package kekmech.ru.mpeiapp;

import android.app.Application;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import io.realm.Realm;
import io.realm.RealmConfiguration;
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
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .name("mpeiapp.db")
                .build()
        );
        DaggerAppComponent.Companion
                .init(this, cicerone)
                .inject(this);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return dispatchingAndroidInjector;
    }
}

