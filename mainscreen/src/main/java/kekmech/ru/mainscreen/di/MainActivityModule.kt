package kekmech.ru.mainscreen.di

import kekmech.ru.addscreen.di.KoinAddFragmentModule
import kekmech.ru.bars.di.KoinBarsModule
import kekmech.ru.core.Router
import kekmech.ru.core.UpdateChecker
import kekmech.ru.feed.di.KoinFeedFragmentModule
import kekmech.ru.mainscreen.ForceUpdateChecker
import kekmech.ru.mainscreen.MainNavRouter
import kekmech.ru.map.di.KoinMapFragmentModule
import kekmech.ru.notes.di.KoinNoteFragmentModule
import kekmech.ru.settings.di.KoinSettingsModule
import kekmech.ru.timetable.di.KoinTimetableFragmentModule
import kekmech.ru.update.di.KoinUpdateModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

val KoinMainActivityModule = module {
    // MainActivity scope
    single { MainNavRouter() } bind Router::class
    single { ForceUpdateChecker(get()) } bind UpdateChecker::class

    // fragment modules
    loadKoinModules(listOf(
        KoinFeedFragmentModule,
        KoinTimetableFragmentModule,
        KoinAddFragmentModule,
        KoinMapFragmentModule,
        KoinUpdateModule,
        KoinBarsModule,
        KoinNoteFragmentModule,
        KoinSettingsModule
    ))
}