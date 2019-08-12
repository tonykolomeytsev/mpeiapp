package kekmech.ru.mpeiapp;

import android.app.Application;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import android.content.Intent;
import kekmech.ru.mainscreen.MainActivity;


public class MPEIApp extends Application {
    private static AppComponent component;

    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = AppComponent.Initializer.init(this);
    }
}

