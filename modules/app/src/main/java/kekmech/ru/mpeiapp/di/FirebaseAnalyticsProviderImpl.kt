package kekmech.ru.mpeiapp.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import kekmech.ru.common_analytics.FirebaseAnalyticsProvider

class FirebaseAnalyticsProviderImpl(private val context: Context) : FirebaseAnalyticsProvider {

    override fun provide(): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
}
