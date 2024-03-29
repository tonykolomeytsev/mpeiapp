package kekmech.ru.lib_analytics_android.dto

sealed class AnalyticsEvent {

    object FirstOpen : AnalyticsEvent()
    object AppOpen : AnalyticsEvent()
    data class ScreenShown(val screenName: String) : AnalyticsEvent()
    data class ScreenVisibilityChanged(val screenName: String, val isVisible: Boolean) : AnalyticsEvent()
    data class Click(val screenName: String, val elementName: String) : AnalyticsEvent()
    data class SettingChanged(
        val screenName: String,
        val settingName: String,
        val settingValue: String
    ) : AnalyticsEvent()
    data class ErrorEvent(val screenName: String, val errorMessage: String) : AnalyticsEvent()
    data class Scroll(val screenName: String, val elementName: String) : AnalyticsEvent()
    data class CustomAction(val screenName: String, val actionName: String) : AnalyticsEvent()
}
