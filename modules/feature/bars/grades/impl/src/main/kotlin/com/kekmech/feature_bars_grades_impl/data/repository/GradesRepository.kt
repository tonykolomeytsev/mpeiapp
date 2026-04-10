package com.kekmech.feature_bars_grades_impl.data.repository

import arrow.core.Ior
import arrow.core.leftIor
import arrow.core.rightIor
import com.kekmech.feature_bars_grades_api.Grades
import com.kekmech.feature_bars_grades_api.ObserveGradesError
import com.kekmech.feature_bars_grades_impl.data.datasource.CachedGradesDataSource
import com.kekmech.feature_bars_grades_impl.data.datasource.RemoteGradesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class GradesRepository(
    private val cachedGradesDataSource: CachedGradesDataSource,
    private val remoteGradesDataSource: RemoteGradesDataSource,
) {

    fun observeGrades(): Flow<Ior<ObserveGradesError, Grades>> = flow {
        val cachedValue = cachedGradesDataSource.get()
        if (cachedValue != null) {
            emit(cachedValue.rightIor())
        }

        val remoteUserFlow: Flow<Ior<ObserveGradesError, Grades>> =
            remoteGradesDataSource.observeGrades()
                .map { either ->
                    either.fold(
                        ifLeft = { error ->
                            cachedValue?.let { Ior.Both(error, it) } ?: error.leftIor()
                        },
                        ifRight = { user ->
                            cachedGradesDataSource.put(user)
                            user.rightIor()
                        }
                    )
                }
        emitAll(remoteUserFlow)
    }
}