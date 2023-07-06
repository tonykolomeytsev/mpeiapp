package kekmech.ru.feature_bars_impl.presentation.screen.main.elm

import kekmech.ru.feature_bars_impl.domain.BarsUserInfo
import kekmech.ru.feature_bars_impl.domain.RemoteBarsConfig

internal data class BarsState(
    //region config
    val config: RemoteBarsConfig? = null,
    val extractJs: String? = null,

    // region data
    val flowState: FlowState = FlowState.UNDEFINED,
    val userInfo: BarsUserInfo? = null,
    val latestLoadedUrl: String? = null,

    // region ui
    val isAfterErrorLoadingConfig: Boolean = false,
    val isLoading: Boolean = true,
    val isBrowserVisible: Boolean = false,
    val isReturnBannerVisible: Boolean = false,
    val webViewUiState: WebViewUiState = WebViewUiState(),
)

internal enum class FlowState {
    NOT_LOGGED_IN,
    LOGGED_IN,
    UNDEFINED,
}

internal data class WebViewUiState(
    val url: String = "",
    val pageTitle: String? = null,
    val isLoading: Boolean = true,
)

internal sealed interface BarsEvent {
    sealed interface Ui : BarsEvent {
        object Init : Ui

        object Action {
            data class PageFinished(val url: String, val pageTitle: String?) : Ui
            object PageStarted : Ui
            object PageLoadingError : Ui
            object Update : Ui
            object ScrollToTop : Ui
        }

        object Click {
            object ShowBrowser : Ui
            object HideBrowser : Ui
            object Settings : Ui
            object SwipeToRefresh : Ui
            object Login : Ui
            data class NotAllowedUrl(val url: String) : Ui
        }

        object Extract {
            data class StudentName(val name: String) : Ui
            data class StudentGroup(val group: String) : Ui
            data class MetaData(val metaDataJson: String) : Ui
            data class Rating(val ratingJson: String) : Ui
            data class Semesters(
                val semestersJson: String,
                val selectedSemesterName: String,
            ) : Ui

            data class Marks(val marksJson: String) : Ui
        }
    }

    sealed interface Internal : BarsEvent {
        data class GetRemoteBarsConfigSuccess(
            val remoteBarsConfig: RemoteBarsConfig,
            val extractJs: String,
        ) : Internal {

            override fun toString(): String =
                "GetRemoteBarsConfigSuccess(remoteBarsConfig=$remoteBarsConfig, ...)"
        }

        data class GetRemoteBarsConfigFailure(val throwable: Throwable) : Internal
        data class ObserveBarsSuccess(val userBars: BarsUserInfo) : Internal
        data class GetLatestLoadedUrlSuccess(val latestLoadedUrl: String?) : Internal
    }
}

internal sealed interface BarsEffect {
    data class LoadPage(val url: String) : BarsEffect
    data class InvokeJs(val js: String) : BarsEffect {

        override fun toString(): String = "InvokeJs(...)"
    }

    object OpenSettings : BarsEffect
    object ShowCommonError : BarsEffect
    data class OpenExternalBrowser(val url: String) : BarsEffect
    object ScrollToTop : BarsEffect
}

internal sealed interface BarsCommand {
    data class SetLatestLoadedUrl(val latestLoadedUrl: String?) : BarsCommand
    object GetLatestLoadedUrl : BarsCommand
    object GetRemoteBarsConfig : BarsCommand
    object ObserveBars : BarsCommand
    data class PushMarks(val marksJson: String) : BarsCommand
    data class PushStudentName(val studentName: String) : BarsCommand
    data class PushStudentGroup(val studentGroup: String) : BarsCommand
    data class PushStudentRating(val ratingJson: String) : BarsCommand
}
