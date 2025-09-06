package kekmech.ru.lib_analytics_android

import com.google.firebase.analytics.FirebaseAnalytics

public interface FirebaseAnalyticsProvider {

    public fun provide(): FirebaseAnalytics
}
