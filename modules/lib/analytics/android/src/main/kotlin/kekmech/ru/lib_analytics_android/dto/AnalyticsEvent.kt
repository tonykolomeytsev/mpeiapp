package kekmech.ru.lib_analytics_android.dto

public sealed class AnalyticsEvent {

    public object FirstOpen : AnalyticsEvent()
    public object AppOpen : AnalyticsEvent()
    public data class ScreenShown(val screenName: String) : AnalyticsEvent()
    public data class ScreenVisibilityChanged(val screenName: String, val isVisible: Boolean) : AnalyticsEvent()
    public data class Click(val screenName: String, val elementName: String) : AnalyticsEvent()
    public data class SettingChanged(
        val screenName: String,
        val settingName: String,
        val settingValue: String
    ) : AnalyticsEvent()
    public data class ErrorEvent(val screenName: String, val errorMessage: String) : AnalyticsEvent()
    public data class Scroll(val screenName: String, val elementName: String) : AnalyticsEvent()
    public data class CustomAction(val screenName: String, val actionName: String) : AnalyticsEvent()
}
