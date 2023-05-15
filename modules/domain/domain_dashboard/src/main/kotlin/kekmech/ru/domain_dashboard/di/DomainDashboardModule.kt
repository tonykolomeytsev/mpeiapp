package kekmech.ru.domain_dashboard.di

import kekmech.ru.domain_dashboard.interactors.GetUpcomingEventsInteractor
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val DomainDashboardModule = module {
    factoryOf(::GetUpcomingEventsInteractor)
}
