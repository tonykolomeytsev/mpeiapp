package kekmech.ru.feature_dashboard

import kekmech.ru.common_analytics.Analytics
import kekmech.ru.common_analytics.AnalyticsImpl
import kekmech.ru.common_analytics.AnalyticsWrapper

class DashboardAnalytics(
    wrapper: AnalyticsWrapper
) : Analytics by AnalyticsImpl(wrapper, "Dashboard")