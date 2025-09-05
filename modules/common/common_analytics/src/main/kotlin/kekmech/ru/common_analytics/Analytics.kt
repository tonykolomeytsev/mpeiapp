package kekmech.ru.common_analytics

import kekmech.ru.common_analytics.dto.AnalyticsEvent

public interface Analytics {

    public val screenName: String

    public fun sendFirstOpen()
    public fun sendAppOpen()
    public fun sendScreenShown()
    public fun sendScreenVisibilityChanged(visibility: Boolean)
    public fun sendClick(elementName: String)
    public fun sendChangeSetting(settingName: String, settingValue: String)
    public fun sendErrorEvent(errorMessage: String)
    public fun sendScroll(elementName: String)
    public fun sendCustomAction(actionName: String)
}

internal class AnalyticsImpl(
    private val wrapper: AnalyticsWrapper,
    override val screenName: String
) : Analytics {

    override fun sendFirstOpen() {
        wrapper.sendEvent(AnalyticsEvent.FirstOpen)
    }

    override fun sendAppOpen() {
        wrapper.sendEvent(AnalyticsEvent.AppOpen)
    }

    override fun sendScreenShown() {
        wrapper.sendEvent(AnalyticsEvent.ScreenShown(screenName))
    }

    override fun sendScreenVisibilityChanged(visibility: Boolean) {
        wrapper.sendEvent(AnalyticsEvent.ScreenVisibilityChanged(screenName, visibility))
    }

    override fun sendClick(elementName: String) {
        wrapper.sendEvent(AnalyticsEvent.Click(screenName, elementName))
    }

    override fun sendChangeSetting(settingName: String, settingValue: String) {
        wrapper.sendEvent(AnalyticsEvent.SettingChanged(screenName, settingName, settingValue))
    }

    override fun sendErrorEvent(errorMessage: String) {
        wrapper.sendEvent(AnalyticsEvent.ErrorEvent(screenName, errorMessage))
    }

    override fun sendScroll(elementName: String) {
        wrapper.sendEvent(AnalyticsEvent.Scroll(screenName, elementName))
    }

    override fun sendCustomAction(actionName: String) {
        wrapper.sendEvent(AnalyticsEvent.CustomAction(screenName, actionName))
    }
}
