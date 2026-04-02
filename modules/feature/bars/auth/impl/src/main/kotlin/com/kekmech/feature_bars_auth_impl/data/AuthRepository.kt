package com.kekmech.feature_bars_auth_impl.data

import arrow.core.Either
import com.kekmech.feature_bars_auth_api.SelectAccountError
import com.kekmech.feature_bars_auth_impl.data.mapper.toDomain
import com.kekmech.feature_bars_auth_impl.data.mapper.toDto
import com.kekmech.feature_bars_auth_impl.domain.Auth2faProvider
import com.kekmech.feature_bars_auth_impl.domain.GetAccountsError
import com.kekmech.feature_bars_auth_impl.domain.InternalAuthAccount
import com.kekmech.feature_bars_auth_impl.domain.InternalAuthState
import com.kekmech.feature_bars_auth_impl.domain.LoginPasswordError
import com.kekmech.feature_bars_auth_impl.domain.TwoFactorRequestError
import com.kekmech.feature_bars_auth_impl.domain.TwoFactorSubmitError
import com.kekmech.feature_bars_auth_impl.domain.UserCredentials
import com.kekmech.perf_sdk.PerfSdk
import com.kekmech.perf_sdk.api.model.auth.AuthError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal class AuthRepository(
    private val perfSdk: PerfSdk,
) {

    private val fatalErrorState = MutableStateFlow(false)

    fun subscribeFatalError(): Flow<Boolean> = fatalErrorState

    fun subscribeAuthState(): Flow<InternalAuthState> = perfSdk.auth.subscribeAuthState()
        .map { it.toDomain() }

    suspend fun login(credentials: UserCredentials): Either<LoginPasswordError, Unit> =
        perfSdk.auth.login(credentials.toDto())
            .mapLeft { error ->
                when (error) {
                    is AuthError.Infra.NetworkError -> LoginPasswordError.Network
                    is AuthError.Domain.InvalidCredentials -> LoginPasswordError.InvalidCredentials
                    else -> {
                        fatalErrorState.emit(true)
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
                        fatalErrorState.emit(true)
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
                        fatalErrorState.emit(true)
                        TwoFactorSubmitError.Fatal
                    }
                }
            }

    suspend fun getAccounts(): Either<GetAccountsError, List<InternalAuthAccount>> =
        perfSdk.auth.getAuthAccounts()
            .mapLeft { error ->
                when (error) {
                    is AuthError.Infra.NetworkError -> GetAccountsError.Network
                    is AuthError.Domain.SessionExpired -> GetAccountsError.SessionExpired
                    else -> {
                        fatalErrorState.emit(true)
                        GetAccountsError.Fatal
                    }
                }
            }
            .map { accounts ->
                accounts.map { it.toDomain() }
            }

    suspend fun selectAccount(id: String): Either<SelectAccountError, Unit> =
        perfSdk.auth.setDefaultAccount(id)
            .mapLeft {
                fatalErrorState.emit(true)
                SelectAccountError.Fatal
            }
}