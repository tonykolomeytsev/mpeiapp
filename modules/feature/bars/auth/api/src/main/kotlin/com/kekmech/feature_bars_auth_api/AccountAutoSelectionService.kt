package com.kekmech.feature_bars_auth_api

import arrow.core.Either

public interface AccountAutoSelectionService {
    public suspend operator fun invoke(): Either<SelectAccountError, Unit>
}