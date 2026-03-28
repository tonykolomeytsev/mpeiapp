package com.kekmech.feature_bars_auth_impl.domain

import com.kekmech.feature_bars_auth_api.AuthState
import com.kekmech.feature_bars_auth_api.SubscribeToAuthStateUseCase
import com.kekmech.feature_bars_auth_impl.data.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SubscribeToAuthStateUseCaseImpl(
    private val authRepository: AuthRepository,
) : SubscribeToAuthStateUseCase {

    override fun invoke(): Flow<AuthState> =
        authRepository.subscribeAuthState()
            .map { state ->
                when (state) {
                    is InternalAuthState.LoggedOut,
                    is InternalAuthState.Awaiting2faProviderSelection,
                    is InternalAuthState.Awaiting2faSubmission,
                    is InternalAuthState.AwaitingAccountSelection,
                    is InternalAuthState.RequestingTwoFactorCode,
                    is InternalAuthState.SubmittingTwoFactorCode,
                    is InternalAuthState.VerifyingCredentials -> AuthState.LoggedOut

                    is InternalAuthState.Idle -> AuthState.Idle
                    is InternalAuthState.LoggedIn -> AuthState.LoggedOut
                    is InternalAuthState.SessionExpired -> AuthState.SessionExpired
                }
            }

}