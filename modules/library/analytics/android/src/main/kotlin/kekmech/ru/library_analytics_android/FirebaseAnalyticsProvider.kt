package kekmech.ru.library_analytics_android

import com.google.firebase.analytics.FirebaseAnalytics

interface FirebaseAnalyticsProvider {

    fun provide(): FirebaseAnalytics
}
