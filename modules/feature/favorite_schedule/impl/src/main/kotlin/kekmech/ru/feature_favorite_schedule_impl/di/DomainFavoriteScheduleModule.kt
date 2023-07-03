package kekmech.ru.feature_favorite_schedule_impl.di

import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.feature_favorite_schedule_api.data.repository.FavoriteScheduleRepository
import kekmech.ru.feature_favorite_schedule_impl.data.database.migrations.MigrationV6V7Impl
import kekmech.ru.feature_favorite_schedule_impl.data.repository.FavoriteScheduleRepositoryImpl
import kekmech.ru.library_app_database.api.MigrationV6V7
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureFavoriteScheduleModule = module {
    factoryOf(::FavoriteScheduleRepositoryImpl) bind FavoriteScheduleRepository::class
    factory { MigrationV6V7Impl() } bindIntoList MigrationV6V7::class
}
