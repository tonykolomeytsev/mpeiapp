package kekmech.ru.mainscreen.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.addscreen.di.AddFragmentModule
import kekmech.ru.feed.di.FeedFragmentModule
import kekmech.ru.mainscreen.MainActivity
import kekmech.ru.settings.di.SettingsDevFragmentModule
import kekmech.ru.settings.di.SettingsFragmentModule
import kekmech.ru.timetable.di.TimetableFragmentModule
import com.example.map.di.MapFragmentModule
import kekmech.ru.update.di.UpdateModule

@ActivityScope
@Subcomponent(
    modules = [
        FeedFragmentModule::class,
        TimetableFragmentModule::class,
        SettingsFragmentModule::class,
        SettingsDevFragmentModule::class,
        AddFragmentModule::class,
        MapFragmentModule::class,
        UpdateModule::class
    ]
)
interface MainActivityComponent : AndroidInjector<MainActivity> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MainActivity>
}