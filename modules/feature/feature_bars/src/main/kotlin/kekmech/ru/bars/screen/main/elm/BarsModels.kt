package kekmech.ru.bars.screen.main.elm

import kekmech.ru.domain_bars.dto.Rating
import kekmech.ru.domain_bars.dto.RemoteBarsConfig
import kekmech.ru.domain_bars.dto.UserBarsInfo

internal data class BarsState(
    //region config
    val config: RemoteBarsConfig? = null,
    val extractJs: String? = null,

    // region data
    val flowState: FlowState = FlowState.UNDEFINED,
    val userInfo: UserBarsInfo? = null,
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
    UNDEFINED
}

internal data class WebViewUiState(
    val url: String = "",
    val pageTitle: String? = null,
    val isLoading: Boolean = true
)

internal sealed class BarsEvent {
    sealed class Wish : BarsEvent() {
        object Init : Wish()

        object Action {
            data class PageFinished(val url: String, val pageTitle: String?) : Wish()
            object PageStarted : Wish()
            object PageLoadingError : Wish()
            object Update : Wish()
            object ScrollToTop : Wish()
        }

        object Click {
            object ShowRating : Wish()
            object ShowBrowser : Wish()
            object HideBrowser : Wish()
            object Settings : Wish()
            object SwipeToRefresh : Wish()
            object Login : Wish()
            data class NotAllowedUrl(val url: String) : Wish()
        }

        object Extract {
            data class StudentName(val name: String) : Wish()
            data class StudentGroup(val group: String) : Wish()
            data class MetaData(val metaDataJson: String) : Wish()
            data class Rating(val ratingJson: String) : Wish()
            data class Semesters(
                val semestersJson: String,
                val selectedSemesterName: String
            ) : Wish()

            data class Marks(val marksJson: String) : Wish()
        }
    }

    sealed class News : BarsEvent() {
        data class GetRemoteBarsConfigSuccess(
            val remoteBarsConfig: RemoteBarsConfig,
            val extractJs: String,
        ) : News() {
            override fun toString(): String =
                "GetRemoteBarsConfigSuccess(remoteBarsConfig=$remoteBarsConfig, ...)"
        }

        data class GetRemoteBarsConfigFailure(val throwable: Throwable) : News()
        data class ObserveBarsSuccess(val userBars: UserBarsInfo) : News()
        data class GetLatestLoadedUrlSuccess(val latestLoadedUrl: String?) : News()
    }
}

internal sealed class BarsEffect {
    data class LoadPage(val url: String) : BarsEffect()
    data class InvokeJs(val js: String) : BarsEffect() {
        override fun toString(): String = "InvokeJs(...)"
    }

    object OpenSettings : BarsEffect()
    object ShowCommonError : BarsEffect()
    data class OpenExternalBrowser(val url: String) : BarsEffect()
    data class OpenRatingDetails(val rating: Rating) : BarsEffect()
    object ScrollToTop : BarsEffect()
}

internal sealed class BarsAction {
    data class SetLatestLoadedUrl(val latestLoadedUrl: String?) : BarsAction()
    object GetLatestLoadedUrl : BarsAction()
    object GetRemoteBarsConfig : BarsAction()
    object ObserveBars : BarsAction()
    data class PushMarks(val marksJson: String) : BarsAction()
    data class PushStudentName(val studentName: String) : BarsAction()
    data class PushStudentGroup(val studentGroup: String) : BarsAction()
    data class PushStudentRating(val ratingJson: String) : BarsAction()
}