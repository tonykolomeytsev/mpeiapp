package com.kekmech.feature_bars_auth_impl.di

import com.kekmech.feature_bars_auth_api.SelectAccountAutomaticallyUseCase
import com.kekmech.feature_bars_auth_api.SubscribeToAuthStateUseCase
import com.kekmech.feature_bars_auth_impl.data.AuthRepository
import com.kekmech.feature_bars_auth_impl.domain.SelectAccountAutomaticallyUseCaseImpl
import com.kekmech.feature_bars_auth_impl.domain.SubscribeToAuthStateUseCaseImpl
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainActor
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainStoreFactory
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordActor
import com.kekmech.feature_bars_auth_impl.presentation.screens.step1.elm.LoginPasswordStoreFactory
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorActor
import com.kekmech.feature_bars_auth_impl.presentation.screens.step2.elm.TwoFactorStoreFactory
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureBarsAuthModule = module {
    factoryOf(::AuthRepository)
    factoryOf(::SubscribeToAuthStateUseCaseImpl) bind SubscribeToAuthStateUseCase::class
    factoryOf(::SelectAccountAutomaticallyUseCaseImpl) bind SelectAccountAutomaticallyUseCase::class

    // region: Presentation
    factoryOf(::BarsAuthMainActor)
    factoryOf(::BarsAuthMainStoreFactory)
    factoryOf(::LoginPasswordActor)
    factoryOf(::LoginPasswordStoreFactory)
    factoryOf(::TwoFactorActor)
    factoryOf(::TwoFactorStoreFactory)
    // endregion: Presentation
}