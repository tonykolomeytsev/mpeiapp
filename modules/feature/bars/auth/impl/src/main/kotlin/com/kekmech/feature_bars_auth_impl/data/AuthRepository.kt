package com.kekmech.feature_bars_auth_impl.data

import arrow.core.Either
import com.kekmech.feature_bars_auth_impl.data.mapper.toDomain
import com.kekmech.feature_bars_auth_impl.data.mapper.toDto
import com.kekmech.feature_bars_auth_impl.domain.Auth2faProvider
import com.kekmech.feature_bars_auth_impl.domain.InternalAuthState
import com.kekmech.feature_bars_auth_impl.domain.LoginPasswordError
import com.kekmech.feature_bars_auth_impl.domain.TwoFactorRequestError
import com.kekmech.feature_bars_auth_impl.domain.TwoFactorSubmitError
import com.kekmech.feature_bars_auth_impl.domain.UserCredentials
import com.kekmech.perf_sdk.PerfSdk
import com.kekmech.perf_sdk.api.model.auth.AuthError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

internal class AuthRepository(
    private val perfSdk: PerfSdk,
) {

    private val fatalErrorFlow = MutableSharedFlow<Unit>(replay = 1)

    fun subscribeFatalError(): Flow<Unit> = fatalErrorFlow

    fun subscribeAuthState(): Flow<InternalAuthState> = perfSdk.auth.subscribeAuthState()
        .map { it.toDomain() }

    suspend fun login(credentials: UserCredentials): Either<LoginPasswordError, Unit> =
        perfSdk.auth.login(credentials.toDto())
            .mapLeft { error ->
                when (error) {
                    is AuthError.Infra.NetworkError -> LoginPasswordError.Network
                    is AuthError.Domain.InvalidCredentials -> LoginPasswordError.InvalidCredentials
                    else -> {
                        fatalErrorFlow.emit(Unit)
                        LoginPasswordError.Fatal
                    }
                }
            }

    suspend fun request2faCode(provider: Auth2faProvider): Either<TwoFactorRequestError, Unit> =
        perfSdk.auth.request2faCode(provider.id)
            .mapLeft { error ->
                when (error) {
                    is AuthError.Infra.NetworkError -> TwoFactorRequestError.Network
                    is AuthError.Domain.CannotRequest2faCode -> TwoFactorRequestError.Message(error.message)
                    else -> {
                        fatalErrorFlow.emit(Unit)
                        TwoFactorRequestError.Fatal
                    }
                }
            }

    suspend fun submit2faCode(code: String): Either<TwoFactorSubmitError, Unit> =
        perfSdk.auth.submit2faCode(code)
            .mapLeft { error ->
                when (error) {
                    is AuthError.Infra.NetworkError -> TwoFactorSubmitError.Network
                    is AuthError.Domain.InvalidCredentials -> TwoFactorSubmitError.WrongCode
                    else -> {
                        fatalErrorFlow.emit(Unit)
                        TwoFactorSubmitError.Fatal
                    }
                }
            }
}