package com.kekmech.feature_bars_user_impl.di

import com.kekmech.feature_bars_user_api.ObserveUserUseCase
import com.kekmech.feature_bars_user_impl.data.datasource.CachedUserDataSource
import com.kekmech.feature_bars_user_impl.data.datasource.RemoteUserDataSource
import com.kekmech.feature_bars_user_impl.data.repository.UserRepository
import com.kekmech.feature_bars_user_impl.domain.ObserveUserUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureBarsUserModule = module {
    factory { CachedUserDataSource() }
    factoryOf(::RemoteUserDataSource)
    factoryOf(::UserRepository)
    factoryOf(::ObserveUserUseCaseImpl) bind ObserveUserUseCase::class
}