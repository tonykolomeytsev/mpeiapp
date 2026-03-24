package com.kekmech.feature_bars_auth_api

import kotlinx.coroutines.flow.StateFlow

public interface SubscribeToAuthStateUseCase {
    public operator fun invoke(): StateFlow<AuthState>
}
