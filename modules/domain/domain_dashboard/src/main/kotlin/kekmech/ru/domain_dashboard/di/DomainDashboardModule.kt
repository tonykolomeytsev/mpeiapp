package kekmech.ru.domain_dashboard.di

import kekmech.ru.domain_dashboard.use_cases.GetUpcomingEventsUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

public val DomainDashboardModule: Module = module {
    factoryOf(::GetUpcomingEventsUseCase)
}
