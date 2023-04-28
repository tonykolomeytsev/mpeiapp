package kekmech.ru.common_analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import kekmech.ru.common_analytics.dto.AnalyticsEvent
import kekmech.ru.domain_schedule.SelectedScheduleAnalyticsProvider

internal class AnalyticsWrapper(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val selectedScheduleAnalyticsProvider: SelectedScheduleAnalyticsProvider,
) {

    fun sendEvent(event: AnalyticsEvent) {
        when (event) {
            is AnalyticsEvent.FirstOpen -> Unit // firebase does it on its own
            is AnalyticsEvent.AppOpen -> firebaseAnalytics.logEvent(Event.APP_OPEN, null)
            is AnalyticsEvent.ScreenShown -> firebaseAnalytics.logEvent(Event.SCREEN_VIEW, mapOf(
                Param.SCREEN_NAME to event.screenName
            ))
            is AnalyticsEvent.ScreenVisibilityChanged -> firebaseAnalytics.logEvent("screen_visibility", mapOf(
                Param.SCREEN_NAME to event.screenName,
                "visibility" to event.isVisible
            ))
            is AnalyticsEvent.Click -> firebaseAnalytics.logEvent("click", mapOf(
                Param.SCREEN_NAME to event.screenName,
                "element_name" to event.elementName
            ))
            is AnalyticsEvent.SettingChanged -> firebaseAnalytics.logEvent("setting_changed", mapOf(
                Param.SCREEN_NAME to event.screenName,
                "setting_name" to event.settingName,
                "setting_value" to event.settingValue
            ))
            is AnalyticsEvent.ErrorEvent -> firebaseAnalytics.logEvent("error_shown", mapOf(
                Param.SCREEN_NAME to event.screenName,
                "error_message" to event.errorMessage
            ))
            is AnalyticsEvent.Scroll -> firebaseAnalytics.logEvent("scroll", mapOf(
                Param.SCREEN_NAME to event.screenName,
                "element_name" to event.elementName
            ))
            is AnalyticsEvent.CustomAction -> firebaseAnalytics.logEvent(event.actionName, mapOf(
                Param.SCREEN_NAME to event.screenName
            ))
        }
    }

    private fun FirebaseAnalytics.logEvent(name: String, params: Map<String, Any>) {
        val bundle = Bundle()
        params.forEach { (k, v) ->
            when (v) {
                is String -> bundle.putString(k, v)
                is Int -> bundle.putInt(k, v)
                is Boolean -> bundle.putBoolean(k, v)
            }
        }
        val scheduleName = selectedScheduleAnalyticsProvider.getSelectedScheduleNameForAnalytics()
        if (!scheduleName.isNullOrBlank()) {
            setUserProperty("schedule_name", scheduleName)
        }
        logEvent(name, bundle)
    }
}
