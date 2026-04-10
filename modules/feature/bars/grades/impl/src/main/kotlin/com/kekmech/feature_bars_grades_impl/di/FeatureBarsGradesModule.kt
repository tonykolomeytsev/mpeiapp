package com.kekmech.feature_bars_grades_impl.di

import com.kekmech.feature_bars_grades_api.ObserveGradesUseCase
import com.kekmech.feature_bars_grades_impl.data.datasource.CachedGradesDataSource
import com.kekmech.feature_bars_grades_impl.data.datasource.RemoteGradesDataSource
import com.kekmech.feature_bars_grades_impl.data.repository.GradesRepository
import com.kekmech.feature_bars_grades_impl.domain.ObserveGradesUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureBarsGradesModule = module {
    factory { CachedGradesDataSource() }
    factoryOf(::RemoteGradesDataSource)
    factoryOf(::GradesRepository)
    factoryOf(::ObserveGradesUseCaseImpl) bind ObserveGradesUseCase::class
}
