package com.kekmech.feature_bars_grades_impl.domain

import arrow.core.Ior
import arrow.core.leftIor
import com.kekmech.feature_bars_auth_api.AccountAutoSelectionService
import com.kekmech.feature_bars_grades_api.Grades
import com.kekmech.feature_bars_grades_api.ObserveGradesError
import com.kekmech.feature_bars_grades_api.ObserveGradesUseCase
import com.kekmech.feature_bars_grades_impl.data.repository.GradesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

internal class ObserveGradesUseCaseImpl(
    private val accountAutoSelectionService: AccountAutoSelectionService,
    private val gradesRepository: GradesRepository,
) : ObserveGradesUseCase {
    override fun invoke(): Flow<Ior<ObserveGradesError, Grades>> = flow {
        accountAutoSelectionService.invoke().mapLeft {
            emit(ObserveGradesError.Internal.leftIor())
            return@flow
        }
        emitAll(gradesRepository.observeGrades())
    }
}