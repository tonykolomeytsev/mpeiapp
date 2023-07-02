package kekmech.ru.domain_favorite_schedule.di

import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import kekmech.ru.domain_favorite_schedule.database.migrations.MigrationV6V7Impl
import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.library_app_database_api.MigrationV6V7
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val DomainFavoriteScheduleModule = module {
    factoryOf(::FavoriteScheduleRepository)

    factory { MigrationV6V7Impl() } bindIntoList MigrationV6V7::class
}
