package com.kekmech.feature_bars_grades_impl.data.datasource

import arrow.core.Either
import arrow.core.left
import com.kekmech.feature_bars_grades_api.Grades
import com.kekmech.feature_bars_grades_api.ObserveGradesError
import com.kekmech.feature_bars_grades_impl.data.mapper.toDomain
import com.kekmech.perf_sdk.PerfSdk
import com.kekmech.perf_sdk.api.model.auth.AuthState
import com.kekmech.perf_sdk.api.model.grades.GradesError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal class RemoteGradesDataSource(
    private val perfSdk: PerfSdk,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun observeGrades(): Flow<Either<ObserveGradesError, Grades>> =
        perfSdk.auth.subscribeAuthState().mapNotNull { state ->
            when (state) {
                is AuthState.LoggedIn -> getGradesInternal()

                is AuthState.LoggedOut,
                is AuthState.SessionExpired -> ObserveGradesError.SessionExpired.left()

                is AuthState.AwaitingAccountSelection -> null
                else -> ObserveGradesError.Internal.left()
            }
        }

    private suspend fun getGradesInternal(): Either<ObserveGradesError, Grades> =
        perfSdk.grades.getGrades()
            .map { it.toDomain() }
            .mapLeft { error ->
                when (error) {
                    is GradesError.Domain.NotLoggedIn,
                    is GradesError.Domain.SessionExpired -> ObserveGradesError.SessionExpired

                    is GradesError.Infra.NetworkError -> ObserveGradesError.Network

                    is GradesError.Domain.AccountIsNotSelected -> {
                        // impossible at this stage
                        // todo: fix sdk in order to make this state impossible
                        ObserveGradesError.Internal
                    }

                    is GradesError.Infra.ClassificationFailed,
                    is GradesError.Infra.ParsingError -> ObserveGradesError.Internal
                }
            }
}