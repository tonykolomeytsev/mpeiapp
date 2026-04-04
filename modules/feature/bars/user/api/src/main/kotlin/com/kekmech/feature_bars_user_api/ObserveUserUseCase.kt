package com.kekmech.feature_bars_user_api

import arrow.core.Ior
import kotlinx.coroutines.flow.Flow

public interface ObserveUserUseCase {
    public operator fun invoke(): Flow<Ior<ObserveUserError, User>>
}