package kekmech.ru.lib_analytics_android

import com.google.firebase.analytics.FirebaseAnalytics

interface FirebaseAnalyticsProvider {

    fun provide(): FirebaseAnalytics
}
