package kekmech.ru.feature_bars_impl.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kekmech.ru.feature_bars_impl.presentation.compose.PreviewDisciplines
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.ShowingFromCacheReason
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsProfile
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsState
import kekmech.ru.lib_elm.Resource
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.emptyFlow

// Previews of the BarsComposeScreen states, the screen itself is in BarsScreen.kt

@PreviewLightDark
@Composable
private fun BarsScreenDataPreview() = BarsScreenPreview(
    BarsState(
        profile = PreviewProfile,
        disciplines = Resource.Data(PreviewDisciplines),
        semesterName = PreviewSemester,
    )
)

@PreviewLightDark
@Composable
private fun BarsScreenSessionExpiredPreview() = BarsScreenPreview(
    BarsState(
        profile = PreviewProfile,
        showingFromCacheReasons = setOf(ShowingFromCacheReason.SessionExpired, ShowingFromCacheReason.NetworkError),
        disciplines = Resource.Data(PreviewDisciplines),
        semesterName = PreviewSemester,
    )
)

@Preview
@Composable
private fun BarsScreenProcessingFailedPreview() = BarsScreenPreview(
    BarsState(
        profile = PreviewProfile,
        showingFromCacheReasons = setOf(ShowingFromCacheReason.ProcessingFailed),
        disciplines = Resource.Data(PreviewDisciplines),
    )
)

@PreviewLightDark
@Composable
private fun BarsScreenLoadingPreview() = BarsScreenPreview(BarsState())

@PreviewLightDark
@Composable
private fun BarsScreenEmptyPreview() = BarsScreenPreview(
    BarsState(
        profile = PreviewProfile,
        disciplines = Resource.Data(emptyList()),
        semesterName = PreviewSemester,
    )
)

@PreviewLightDark
@Composable
private fun BarsScreenLoginRequiredPreview() = BarsScreenPreview(BarsState(isLoginRequired = true))

@Preview
@Composable
private fun BarsScreenLongNamePreview() = BarsScreenPreview(
    BarsState(
        profile = Resource.Data(BarsProfile(name = "Константинопольская Александра-Мария", group = "ИЭ-07м-25")),
        disciplines = Resource.Data(PreviewDisciplines.take(2)),
    )
)

@Preview(device = Devices.TABLET)
@Composable
private fun BarsScreenTabletPreview() = BarsScreenDataPreview()

@Composable
private fun BarsScreenPreview(state: BarsState) {
    MpeixTheme {
        BarsComposeScreen(
            onAccept = { /* no-op */ },
            state = state,
            effects = emptyFlow(),
        )
    }
}

private val PreviewProfile = Resource.Data(BarsProfile(name = "Антон Коломейцев", group = "С-12-16"))
private const val PreviewSemester = "2025/2026, Весенний семестр"
