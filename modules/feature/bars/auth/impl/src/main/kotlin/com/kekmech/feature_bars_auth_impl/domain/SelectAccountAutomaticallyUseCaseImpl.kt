package com.kekmech.feature_bars_auth_impl.domain

import arrow.core.Either
import arrow.core.raise.either
import com.kekmech.feature_bars_auth_api.SelectAccountAutomaticallyUseCase
import com.kekmech.feature_bars_auth_api.SelectAccountError
import com.kekmech.feature_bars_auth_impl.data.AuthRepository
import kotlinx.coroutines.flow.first

internal class SelectAccountAutomaticallyUseCaseImpl(
    private val authRepository: AuthRepository,
) : SelectAccountAutomaticallyUseCase {

    override suspend fun invoke(): Either<SelectAccountError, Unit> = either {
        if (authRepository.subscribeAuthState().first() is InternalAuthState.LoggedIn) {
            return@either
        }

        val accounts = authRepository.getAccounts()
            .mapLeft { error ->
                when (error) {
                    GetAccountsError.Network -> SelectAccountError.Network
                    GetAccountsError.Fatal -> SelectAccountError.Fatal
                    GetAccountsError.SessionExpired -> SelectAccountError.SessionExpired
                }
            }
            .bind()

        val selectedAccount = accounts
            .maxByOrNull { it.group.yearOfAdmission() ?: 0 }

        if (selectedAccount?.group?.yearOfAdmission() == null) {
            raise(SelectAccountError.Fatal)
        }

        authRepository.selectAccount(selectedAccount.id)
    }

    private fun String.yearOfAdmission(): Int? =
        trim().substringAfterLast('-').toIntOrNull()
}