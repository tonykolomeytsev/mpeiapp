package kekmech.ru.feature_bars_impl.presentation.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.tonykolomeytsev.mpeix.sdk.Discipline
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import kekmech.ru.feature_bars_impl.data.repository.BarsProfileIdRepository
import kekmech.ru.feature_bars_impl.navigation.BarsDetailsScreenKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/** Reads the discipline from the SDK cache, so the key stays small and the sheet follows refreshes. */
@OptIn(ExperimentalCoroutinesApi::class)
internal class BarsDetailsViewModel(
    key: BarsDetailsScreenKey,
    sdk: MpeixSdk,
    barsProfileIdRepository: BarsProfileIdRepository,
) : ViewModel() {

    /** Null until the first emission of the SDK. */
    val discipline: StateFlow<Discipline?> = barsProfileIdRepository.currentProfileId
        .filterNotNull()
        .flatMapLatest { sdk.bars.study.academicPerformance(it) }
        .map { it.value?.current?.disciplines?.getOrNull(key.index) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
