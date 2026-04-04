package com.kekmech.feature_bars_user_impl.data.datasource

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import com.kekmech.feature_bars_user_api.ObserveUserError
import com.kekmech.feature_bars_user_api.User
import com.kekmech.perf_sdk.PerfSdk
import com.kekmech.perf_sdk.api.model.auth.AuthAccount
import com.kekmech.perf_sdk.api.model.auth.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import com.kekmech.perf_sdk.api.model.auth.User as SdkUser

internal class RemoteUserDataSource(
    private val perfSdk: PerfSdk,
) {

    fun observeUser(): Flow<Either<ObserveUserError, User>> = perfSdk.auth.subscribeAuthState()
        .mapNotNull { state ->
            when (state) {
                // valid login process:
                is AuthState.Idle,
                is AuthState.Awaiting2faProviderSelection,
                is AuthState.Awaiting2faSubmission,
                is AuthState.AwaitingAccountSelection,
                is AuthState.RequestingTwoFactorCode,
                is AuthState.SubmittingTwoFactorCode,
                is AuthState.VerifyingCredentials -> null

                // invalid states:
                is AuthState.SessionExpired,
                is AuthState.LoggedOut -> ObserveUserError.SessionExpired.left()

                // single valid state
                is AuthState.LoggedIn -> state.user.right()
            }
        }
        .map { result -> result.flatMap { it.toDomain() } }

    private fun SdkUser.toDomain(): Either<ObserveUserError, User> = either {
        val selectedAccount = accounts.getActualAccount()
        ensure(selectedAccount != null) { ObserveUserError.Internal }

        User(
            login = username,
            name = selectedAccount.name,
            group = selectedAccount.group,
        )
    }

    private fun Set<AuthAccount>.getActualAccount(): AuthAccount? =
        maxByOrNull { it.name.substringAfterLast('-').toIntOrNull() ?: 0 }
}