@file:OptIn(MpeixSdkImplementation::class)

package kekmech.ru.feature_bars_impl

import android.content.SharedPreferences
import io.github.tonykolomeytsev.mpeix.sdk.AcademicPerformance
import io.github.tonykolomeytsev.mpeix.sdk.Account
import io.github.tonykolomeytsev.mpeix.sdk.AccountId
import io.github.tonykolomeytsev.mpeix.sdk.AccountStatus
import io.github.tonykolomeytsev.mpeix.sdk.Absence
import io.github.tonykolomeytsev.mpeix.sdk.AttendanceSummary
import io.github.tonykolomeytsev.mpeix.sdk.BarsAttendanceService
import io.github.tonykolomeytsev.mpeix.sdk.BarsAuthService
import io.github.tonykolomeytsev.mpeix.sdk.BarsProfileService
import io.github.tonykolomeytsev.mpeix.sdk.BarsService
import io.github.tonykolomeytsev.mpeix.sdk.BarsStudyService
import io.github.tonykolomeytsev.mpeix.sdk.Completeness
import io.github.tonykolomeytsev.mpeix.sdk.DiagnosticId
import io.github.tonykolomeytsev.mpeix.sdk.DiagnosticsService
import io.github.tonykolomeytsev.mpeix.sdk.LoginAttempt
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdkImplementation
import io.github.tonykolomeytsev.mpeix.sdk.Profile
import io.github.tonykolomeytsev.mpeix.sdk.ProfileId
import io.github.tonykolomeytsev.mpeix.sdk.RefreshResult
import io.github.tonykolomeytsev.mpeix.sdk.RefreshState
import io.github.tonykolomeytsev.mpeix.sdk.Resource
import io.github.tonykolomeytsev.mpeix.sdk.SdkError
import io.github.tonykolomeytsev.mpeix.sdk.Semester
import io.github.tonykolomeytsev.mpeix.sdk.SemesterId
import io.github.tonykolomeytsev.mpeix.sdk.SemesterInfo
import io.github.tonykolomeytsev.mpeix.sdk.Subaccount
import io.github.tonykolomeytsev.mpeix.sdk.SubaccountId
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import kekmech.ru.feature_bars_impl.data.repository.BarsProfileIdRepository
import kekmech.ru.feature_bars_impl.sync.SyncBarsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

internal class SyncBarsUseCaseTest : StringSpec({

    "Every signed in account is refreshed with its last subaccount, expired and signed out ones are skipped" {
        val sdk = FakeSdk(
            account("a", AccountStatus.SignedIn, "a1", "a2"),
            account("b", AccountStatus.SessionExpired, "b1"),
            account("c", AccountStatus.SignedOut, "c1"),
            account("d", AccountStatus.Unfinished),
        )

        sync(sdk).shouldBeTrue()

        sdk.study.refreshed shouldContainExactly listOf(profile("a", "a2"))
        sdk.attendance.refreshed shouldContainExactly listOf(profile("a", "a2") to SemesterId("27"))
    }

    "The current profile is refreshed with its own subaccount" {
        val sdk = FakeSdk(account("a", AccountStatus.SignedIn, "a1", "a2"))

        sync(sdk, current = profile("a", "a1")).shouldBeTrue()

        sdk.study.refreshed shouldContainExactly listOf(profile("a", "a1"))
    }

    "Refreshed by the user lately: no requests" {
        val sdk = FakeSdk(account("a", AccountStatus.SignedIn, "a1"), updatedAt = Now - 2.hours)

        sync(sdk).shouldBeTrue()

        sdk.study.refreshed.shouldBeEmpty()
        sdk.attendance.refreshed.shouldBeEmpty()
    }

    "Refreshed by the user a day ago: refreshed again" {
        val sdk = FakeSdk(account("a", AccountStatus.SignedIn, "a1"), updatedAt = Now - 1.days)

        sync(sdk).shouldBeTrue()

        sdk.study.refreshed shouldContainExactly listOf(profile("a", "a1"))
    }

    "No connection: try again later, the other accounts are still refreshed" {
        val sdk = FakeSdk(account("a", AccountStatus.SignedIn, "a1"), account("b", AccountStatus.SignedIn, "b1"))
        sdk.study.results[profile("a", "a1")] = RefreshResult.Failed(SdkError.NoConnection(Diagnostic))

        sync(sdk).shouldBeFalse()

        sdk.study.refreshed shouldContainExactly listOf(profile("a", "a1"), profile("b", "b1"))
        sdk.attendance.refreshed shouldContainExactly listOf(profile("b", "b1") to SemesterId("27"))
    }

    "Attendance timed out: try again later" {
        val sdk = FakeSdk(account("a", AccountStatus.SignedIn, "a1"))
        sdk.attendance.result = RefreshResult.Failed(SdkError.Timeout(Diagnostic))

        sync(sdk).shouldBeFalse()
    }

    "The session expired during the refresh: no attendance request, and no retry today" {
        val sdk = FakeSdk(account("a", AccountStatus.SignedIn, "a1"))
        sdk.study.results[profile("a", "a1")] =
            RefreshResult.Failed(SdkError.SessionExpired(Diagnostic, AccountId("a")))

        sync(sdk).shouldBeTrue()

        sdk.attendance.refreshed.shouldBeEmpty()
    }

    "The refreshed grades brought a new semester: its attendance is refreshed" {
        val sdk = FakeSdk(account("a", AccountStatus.SignedIn, "a1"))
        sdk.study.semesterAfterRefresh = SemesterId("28")

        sync(sdk).shouldBeTrue()

        sdk.attendance.refreshed shouldContainExactly listOf(profile("a", "a1") to SemesterId("28"))
    }
}) {

    private companion object {

        val Now: Instant = Instant.parse("2026-11-20T12:00:00Z")
        val Diagnostic = DiagnosticId("TEST-0001")

        suspend fun sync(sdk: FakeSdk, current: ProfileId? = null): Boolean {
            val repository = BarsProfileIdRepository(sdk, FakePreferences(current?.encode()))
            return SyncBarsUseCase(sdk, repository) { Now }()
        }

        fun profile(account: String, subaccount: String) = ProfileId(AccountId(account), SubaccountId(subaccount))

        fun account(id: String, status: AccountStatus, vararg subaccounts: String) = Account(
            id = AccountId(id),
            username = id,
            displayName = null,
            status = status,
            subaccounts = subaccounts.map { Subaccount(SubaccountId(it), title = it, group = null, educationLevel = null) },
            lastSignedInAt = null,
        )
    }
}

private fun grades(semesterId: SemesterId, updatedAt: Instant?) = Resource(
    value = AcademicPerformance(
        semesters = emptyList(),
        current = Semester(SemesterInfo(semesterId, "2026/2027, Осенний семестр", null, null), emptyList()),
    ),
    updatedAt = updatedAt,
    refresh = RefreshState.Idle,
    completeness = Completeness.Complete,
)

private class FakeSdk(vararg accounts: Account, updatedAt: Instant? = null) : MpeixSdk {
    val auth = FakeAuth(accounts.toList())
    val study = FakeStudy(updatedAt)
    val attendance = FakeAttendance()

    override val bars: BarsService = object : BarsService {
        override val auth: BarsAuthService = this@FakeSdk.auth
        override val profile: BarsProfileService get() = error("not used")
        override val study: BarsStudyService = this@FakeSdk.study
        override val attendance: BarsAttendanceService = this@FakeSdk.attendance
    }
    override val diagnostics: DiagnosticsService get() = error("not used")
}

private class FakeAuth(accounts: List<Account>) : BarsAuthService {
    override val accounts: Flow<List<Account>> = flowOf(accounts)
    override fun account(id: AccountId): Flow<Account?> = accounts.map { list -> list.firstOrNull { it.id == id } }
    override fun startLogin(): LoginAttempt = error("not used")
    override fun resumeLogin(accountId: AccountId): LoginAttempt = error("not used")
    override suspend fun signOut(accountId: AccountId) = error("not used")
    override suspend fun removeAccount(accountId: AccountId) = error("not used")
}

private class FakeStudy(private val updatedAt: Instant?) : BarsStudyService {
    val refreshed = mutableListOf<ProfileId>()
    val results = mutableMapOf<ProfileId, RefreshResult>()
    var semesterAfterRefresh = SemesterId("27")
    private val grades = mutableMapOf<ProfileId, MutableStateFlow<Resource<AcademicPerformance>>>()

    override fun academicPerformance(id: ProfileId): Flow<Resource<AcademicPerformance>> = gradesOf(id)

    override suspend fun refresh(id: ProfileId): RefreshResult {
        refreshed += id
        val result = results[id] ?: RefreshResult.Updated
        if (result == RefreshResult.Updated) gradesOf(id).value = grades(semesterAfterRefresh, Instant.DISTANT_FUTURE)
        return result
    }

    override fun semester(id: ProfileId, semesterId: SemesterId): Flow<Resource<Semester>> = error("not used")
    override suspend fun refresh(id: ProfileId, semesterId: SemesterId): RefreshResult = error("not used")

    private fun gradesOf(id: ProfileId) = grades.getOrPut(id) { MutableStateFlow(grades(SemesterId("27"), updatedAt)) }
}

private class FakeAttendance : BarsAttendanceService {
    val refreshed = mutableListOf<Pair<ProfileId, SemesterId>>()
    var result: RefreshResult = RefreshResult.Updated

    override suspend fun refreshSummary(id: ProfileId, semesterId: SemesterId): RefreshResult {
        refreshed += id to semesterId
        return result
    }

    override fun summary(id: ProfileId, semesterId: SemesterId): Flow<Resource<AttendanceSummary>> = error("not used")
    override fun absences(id: ProfileId, semesterId: SemesterId): Flow<Resource<List<Absence>>> = error("not used")
    override suspend fun refreshAbsences(id: ProfileId, semesterId: SemesterId): RefreshResult = error("not used")
}

/** Only the saved current profile is read */
private class FakePreferences(private val currentProfileId: String?) : SharedPreferences {
    override fun getString(key: String?, defValue: String?): String? = currentProfileId ?: defValue
    override fun getAll(): Map<String, *> = error("not used")
    override fun getStringSet(key: String?, defValues: Set<String>?): Set<String>? = error("not used")
    override fun getInt(key: String?, defValue: Int): Int = error("not used")
    override fun getLong(key: String?, defValue: Long): Long = error("not used")
    override fun getFloat(key: String?, defValue: Float): Float = error("not used")
    override fun getBoolean(key: String?, defValue: Boolean): Boolean = error("not used")
    override fun contains(key: String?): Boolean = error("not used")
    override fun edit(): SharedPreferences.Editor = error("not used")
    override fun registerOnSharedPreferenceChangeListener(l: SharedPreferences.OnSharedPreferenceChangeListener?) = Unit
    override fun unregisterOnSharedPreferenceChangeListener(l: SharedPreferences.OnSharedPreferenceChangeListener?) = Unit
}
