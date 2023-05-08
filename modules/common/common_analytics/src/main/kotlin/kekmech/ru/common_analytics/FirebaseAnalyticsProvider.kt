package kekmech.ru.common_analytics

import com.google.firebase.analytics.FirebaseAnalytics

interface FirebaseAnalyticsProvider {

    fun provide(): FirebaseAnalytics
}
