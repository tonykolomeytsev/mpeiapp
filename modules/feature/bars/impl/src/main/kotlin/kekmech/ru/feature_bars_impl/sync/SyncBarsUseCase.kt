package kekmech.ru.feature_bars_impl.sync

import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import io.github.tonykolomeytsev.mpeix.sdk.ProfileId
import io.github.tonykolomeytsev.mpeix.sdk.RefreshResult
import io.github.tonykolomeytsev.mpeix.sdk.SdkError
import kekmech.ru.feature_bars_impl.data.repository.BarsProfileIdRepository
import kotlinx.coroutines.flow.first
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

/**
 * Refreshes what the BARS tab shows (the grades page and the attendance total) of every signed in account,
 * like pull-to-refresh does. Each site request renews the session cookie of the account (7 days since the
 * last request), so the session of a user who doesn't open the app for days doesn't expire, and the cache
 * stays current if it does expire later.
 *
 * An account is refreshed as the profile it opens with, see [BarsProfileIdRepository.profileIdToOpen].
 * Expired and signed out accounts are skipped: the SDK has no session to send.
 */
internal class SyncBarsUseCase(
    private val sdk: MpeixSdk,
    private val barsProfileIdRepository: BarsProfileIdRepository,
    private val now: () -> Instant = { Clock.System.now() },
) {

    /**
     * @return false if some refresh failed for a reason that may pass soon: no connection, a timeout, the site is down
     */
    suspend operator fun invoke(): Boolean {
        val profiles = sdk.bars.auth.accounts.first().mapNotNull(barsProfileIdRepository::profileIdToOpen)
        // One account at a time, there is no hurry in the background. Every one is tried even if one fails
        return profiles.map { refresh(it) }.all { it }
    }

    private suspend fun refresh(id: ProfileId): Boolean {
        // The user refreshed it lately: the session was renewed by that request
        val updatedAt = sdk.bars.study.academicPerformance(id).first().updatedAt
        if (updatedAt != null && now() - updatedAt < FreshFor) return true

        // The profile comes from the same page
        val grades = sdk.bars.study.refresh(id)
        if (grades is RefreshResult.Failed) return !grades.error.mayPass()

        // After the refresh: the grades may have brought a new semester
        val semesterId = sdk.bars.study.academicPerformance(id).first().value?.current?.info?.id ?: return true
        val attendance = sdk.bars.attendance.refreshSummary(id, semesterId)
        return attendance !is RefreshResult.Failed || !attendance.error.mayPass()
    }

    /** The session is over or the site changed: another try today won't help */
    private fun SdkError.mayPass(): Boolean =
        when (this) {
            is SdkError.NoConnection,
            is SdkError.Timeout,
            is SdkError.SiteUnavailable -> true

            else -> false
        }

    private companion object {

        val FreshFor = 12.hours
    }
}
