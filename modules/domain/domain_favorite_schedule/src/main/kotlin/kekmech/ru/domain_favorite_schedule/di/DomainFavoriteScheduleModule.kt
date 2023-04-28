package kekmech.ru.domain_favorite_schedule.di

import kekmech.ru.domain_favorite_schedule.FavoriteScheduleRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val DomainFavoriteScheduleModule = module {
    factoryOf(::FavoriteScheduleRepository)
}
