package com.kekmech.feature_bars_user_impl.domain

import arrow.core.Ior
import arrow.core.leftIor
import com.kekmech.feature_bars_auth_api.AccountAutoSelectionService
import com.kekmech.feature_bars_user_api.ObserveUserError
import com.kekmech.feature_bars_user_api.ObserveUserUseCase
import com.kekmech.feature_bars_user_api.User
import com.kekmech.feature_bars_user_impl.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

internal class ObserveUserUseCaseImpl(
    private val accountAutoSelectionService: AccountAutoSelectionService,
    private val userRepository: UserRepository,
) : ObserveUserUseCase {

    override fun invoke(): Flow<Ior<ObserveUserError, User>> = flow {
        accountAutoSelectionService.invoke().mapLeft {
            emit(ObserveUserError.Internal.leftIor())
            return@flow
        }
        emitAll(userRepository.observeUser())
    }
}