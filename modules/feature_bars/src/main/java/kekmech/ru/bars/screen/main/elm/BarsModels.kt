package kekmech.ru.bars.screen.main.elm

import kekmech.ru.domain_bars.dto.RemoteBarsConfig
import kekmech.ru.domain_bars.dto.UserBarsInfo

internal data class BarsState(
    val isLoggedIn: Boolean? = null,
    val isBrowserShownForce: Boolean = false,
    val config: RemoteBarsConfig? = null,
    val isAfterErrorLoadingConfig: Boolean = false,
    val userBars: UserBarsInfo? = null,
    val isAfterErrorLoadingUserBars: Boolean = false,
    val isLoading: Boolean = true
) {

    val isBrowserShown: Boolean = (isLoggedIn == false) || isBrowserShownForce
}

internal sealed class BarsEvent {
    sealed class Wish : BarsEvent() {
        object Init : Wish()

        object Action {
            object Update : Wish()
            data class PageFinished(val url: String) : Wish()
        }

        object Click {
            object ShowBrowser : Wish()
            object HideBrowser : Wish()

            object Notes : Wish()
            object Settings : Wish()

            object Logout : Wish()
            object SwipeToRefresh : Wish()
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
        data class GetRemoteBarsConfigSuccess(val remoteBarsConfig: RemoteBarsConfig) : News()
        data class GetRemoteBarsConfigFailure(val throwable: Throwable) : News()
        data class ObserveBarsSuccess(val userBars: UserBarsInfo) : News()
        data class ObserveBarsFailure(val throwable: Throwable) : News()
    }
}

internal sealed class BarsEffect {
    data class LoadPage(val url: String) : BarsEffect()
    data class InvokeJs(val js: String) : BarsEffect()
    object OpenAllNotes : BarsEffect()
    object OpenSettings : BarsEffect()
}

internal sealed class BarsAction {
    object GetRemoteBarsConfig : BarsAction()
    object ObserveBars : BarsAction()
    data class PushMarks(val marksJson: String) : BarsAction()
    data class PushStudentName(val studentName: String) : BarsAction()
    data class PushStudentGroup(val studentGroup: String) : BarsAction()
}