package kekmech.ru.feature_bars_impl.presentation.screen.attendance

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.tonykolomeytsev.mpeix.sdk.Absence
import io.github.tonykolomeytsev.mpeix.sdk.DisciplineAttendance
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import io.github.tonykolomeytsev.mpeix.sdk.RefreshState
import io.github.tonykolomeytsev.mpeix.sdk.SemesterId
import kekmech.ru.feature_bars_impl.data.repository.BarsProfileIdRepository
import kekmech.ru.feature_bars_impl.data.sdk.SdkErrorException
import kekmech.ru.feature_bars_impl.navigation.BarsAttendanceScreenKey
import kekmech.ru.feature_bars_impl.presentation.compose.olderOf
import kekmech.ru.lib_elm.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Instant
import io.github.tonykolomeytsev.mpeix.sdk.Resource as SdkResource

@Immutable
internal data class BarsAttendanceState(
    /** Site order. */
    val disciplines: Resource<List<DisciplineAttendance>> = Resource.Loading,
    /** Newest first, as the SDK gives them. */
    val absences: Resource<List<Absence>> = Resource.Loading,
    /** Only a pull-to-refresh, the automatic first load shows the placeholders instead. */
    val isRefreshing: Boolean = false,
    /** The older of the two pages, null until one of them is loaded. */
    val updatedAt: Instant? = null,
)

/**
 * Attendance of the semester from the key: the summary and the absences are two pages with their own caches,
 * so each section loads and fails on its own.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class BarsAttendanceViewModel(
    key: BarsAttendanceScreenKey,
    private val sdk: MpeixSdk,
    private val barsProfileIdRepository: BarsProfileIdRepository,
) : ViewModel() {

    private val semesterId = SemesterId(key.semesterId)
    private val isRefreshing = MutableStateFlow(false)

    val state: StateFlow<BarsAttendanceState> = barsProfileIdRepository.currentProfileId
        .filterNotNull()
        .flatMapLatest { id ->
            combine(
                sdk.bars.attendance.summary(id, semesterId),
                sdk.bars.attendance.absences(id, semesterId),
                isRefreshing,
            ) { summary, absences, isRefreshing ->
                BarsAttendanceState(
                    disciplines = summary.toUiResource { it.disciplines },
                    absences = absences.toUiResource { it },
                    isRefreshing = isRefreshing,
                    updatedAt = olderOf(summary.updatedAt, absences.updatedAt),
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, BarsAttendanceState())

    /** Concurrent refreshes of the same page share one request in the SDK. */
    fun refresh() {
        val id = barsProfileIdRepository.currentProfileId.value ?: return
        viewModelScope.launch {
            isRefreshing.value = true
            coroutineScope {
                launch { sdk.bars.attendance.refreshSummary(id, semesterId) }
                launch { sdk.bars.attendance.refreshAbsences(id, semesterId) }
            }
            isRefreshing.value = false
        }
    }

    fun reloadDisciplines() {
        val id = barsProfileIdRepository.currentProfileId.value ?: return
        viewModelScope.launch { sdk.bars.attendance.refreshSummary(id, semesterId) }
    }

    fun reloadAbsences() {
        val id = barsProfileIdRepository.currentProfileId.value ?: return
        viewModelScope.launch { sdk.bars.attendance.refreshAbsences(id, semesterId) }
    }

    /**
     * A cached value is always shown, even with a failed refresh. Without one, a failed first load is an error
     * (otherwise the section would wait forever) and anything else is still loading.
     */
    private fun <T, R : Any> SdkResource<T>.toUiResource(transform: (T) -> R): Resource<R> {
        value?.let { return Resource.Data(transform(it)) }
        val failed = refresh as? RefreshState.Failed ?: return Resource.Loading
        return Resource.Error(SdkErrorException(failed.error))
    }
}
