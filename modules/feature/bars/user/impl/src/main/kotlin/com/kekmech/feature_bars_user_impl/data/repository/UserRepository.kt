package com.kekmech.feature_bars_user_impl.data.repository

import arrow.core.Ior
import arrow.core.leftIor
import arrow.core.rightIor
import com.kekmech.feature_bars_user_api.ObserveUserError
import com.kekmech.feature_bars_user_api.User
import com.kekmech.feature_bars_user_impl.data.datasource.CachedUserDataSource
import com.kekmech.feature_bars_user_impl.data.datasource.RemoteUserDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class UserRepository(
    private val cachedUserDataSource: CachedUserDataSource,
    private val remoteUserDataSource: RemoteUserDataSource,
) {

    fun observeUser(): Flow<Ior<ObserveUserError, User>> = flow {
        val cachedValue = cachedUserDataSource.get()
        if (cachedValue != null) {
            emit(cachedValue.rightIor())
        }

        val remoteUserFlow: Flow<Ior<ObserveUserError, User>> = remoteUserDataSource.observeUser()
            .map { either ->
                either.fold(
                    ifLeft = { error ->
                        cachedValue?.let { Ior.Both(error, it) } ?: error.leftIor()
                    },
                    ifRight = { user ->
                        cachedUserDataSource.put(user)
                        user.rightIor()
                    }
                )
            }
        emitAll(remoteUserFlow)
    }
}