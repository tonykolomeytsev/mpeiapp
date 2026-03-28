package com.kekmech.feature_bars_auth_api

import kotlinx.coroutines.flow.Flow

public interface SubscribeToAuthStateUseCase {
    public operator fun invoke(): Flow<AuthState>
}
