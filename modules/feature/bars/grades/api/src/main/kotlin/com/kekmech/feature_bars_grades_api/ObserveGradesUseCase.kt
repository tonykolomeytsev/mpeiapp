package com.kekmech.feature_bars_grades_api

import arrow.core.Ior
import kotlinx.coroutines.flow.Flow

public interface ObserveGradesUseCase {
    public operator fun invoke(): Flow<Ior<ObserveGradesError, Grades>>
}
