package kekmech.ru.feature_schedule_impl.di

import androidx.room.Room
import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.feature_schedule_api.PreheatSelectedScheduleProvider
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi
import kekmech.ru.feature_schedule_api.ScheduleSyncScheduler
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.data.repository.ScheduleSearchRepository
import kekmech.ru.feature_schedule_api.domain.usecase.HasSelectedScheduleUseCase
import kekmech.ru.feature_schedule_api.domain.usecase.ObserveCurrentScheduleUseCase
import kekmech.ru.feature_schedule_impl.data.database.ScheduleCacheDatabase
import kekmech.ru.feature_schedule_impl.data.datasource.CachedScheduleSource
import kekmech.ru.feature_schedule_impl.data.datasource.SelectedScheduleSource
import kekmech.ru.feature_schedule_impl.data.network.ScheduleService
import kekmech.ru.feature_schedule_impl.data.repository.ScheduleRepositoryImpl
import kekmech.ru.feature_schedule_impl.data.repository.ScheduleSearchRepositoryImpl
import kekmech.ru.feature_schedule_impl.domain.usecase.HasSelectedScheduleUseCaseImpl
import kekmech.ru.feature_schedule_impl.launcher.ScheduleFeatureApiImpl
import kekmech.ru.feature_schedule_impl.navigation.ScheduleNavEntries
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleActor
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleStoreFactory
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleActor
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleStoreFactory
import kekmech.ru.feature_schedule_impl.sync.ScheduleSyncInitializer
import kekmech.ru.feature_schedule_impl.sync.ScheduleSyncSchedulerImpl
import kekmech.ru.feature_schedule_impl.sync.SyncSchedulesUseCase
import kekmech.ru.lib_analytics_api.SelectedScheduleAnalyticsProvider
import kekmech.ru.lib_app_lifecycle.AppLifecycleObserver
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_network.buildApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit
import java.time.Clock
import java.time.ZoneId

val FeatureScheduleModule = module {
    factoryOf(::ScheduleFeatureApiImpl) bind ScheduleFeatureApi::class
    factoryOf(::FindScheduleActor)

    factoryOf(::FindScheduleStoreFactory) bind FindScheduleStoreFactory::class
    singleOf(::ScheduleStoreFactory)
    factoryOf(::ScheduleActor)
    factoryOf(::ScheduleDependencies)

    factory { get<Retrofit.Builder>().buildApi<ScheduleService>() }
    single {
        Room
            .databaseBuilder(
                context = get(),
                klass = ScheduleCacheDatabase::class.java,
                name = ScheduleCacheDatabase.Name,
            )
            // It's a cache: drop it on any schema change and download again, never migrate
            .fallbackToDestructiveMigration(dropAllTables = true)
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .build()
    }
    factory { get<ScheduleCacheDatabase>().scheduleWeekDao() }
    single {
        CachedScheduleSource(
            scheduleService = get(),
            scheduleWeekDao = get(),
            json = get(),
            // application scope: downloads outlive the screens that started them
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
            dispatcher = Dispatchers.IO,
            clock = Clock.system(ZoneId.of("Europe/Moscow")),
        )
    }
    // background sync
    factory { SyncSchedulesUseCase(scheduleRepository = get(), favoriteScheduleRepository = get()) }
    factoryOf(::ScheduleSyncSchedulerImpl) bind ScheduleSyncScheduler::class
    factoryOf(::ScheduleSyncInitializer) bindIntoList AppLifecycleObserver::class

    singleOf(::ScheduleRepositoryImpl) binds arrayOf(
        ScheduleRepository::class,
        SelectedScheduleAnalyticsProvider::class,
        PreheatSelectedScheduleProvider::class,
    )
    factoryOf(::ScheduleSearchRepositoryImpl) bind ScheduleSearchRepository::class
    factoryOf(::ObserveCurrentScheduleUseCase)
    factoryOf(::SelectedScheduleSource)
    factoryOf(::HasSelectedScheduleUseCaseImpl) bind HasSelectedScheduleUseCase::class
    factoryOf(::ScheduleNavEntries) bindIntoList NavEntries::class
}
