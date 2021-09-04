package kekmech.ru.bars.screen.main.elm

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
)

internal enum class FlowState {
    NOT_LOGGED_IN,
    LOGGED_IN,
    UNDEFINED
}

internal sealed class BarsEvent {
    sealed class Wish : BarsEvent() {
        object Init : Wish()

        object Action {
            data class PageFinished(val url: String) : Wish()
            object Update : Wish()
        }

        object Click {
            object ShowBrowser : Wish()
            object HideBrowser : Wish()
            object Settings : Wish()
            object SwipeToRefresh : Wish()
            object Login : Wish()
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
    object OpenAllNotes : BarsEffect()
    object OpenSettings : BarsEffect()
}

internal sealed class BarsAction {
    data class SetLatestLoadedUrl(val latestLoadedUrl: String?) : BarsAction()
    object GetLatestLoadedUrl : BarsAction()
    object GetRemoteBarsConfig : BarsAction()
    object ObserveBars : BarsAction()
    data class PushMarks(val marksJson: String) : BarsAction()
    data class PushStudentName(val studentName: String) : BarsAction()
    data class PushStudentGroup(val studentGroup: String) : BarsAction()
}