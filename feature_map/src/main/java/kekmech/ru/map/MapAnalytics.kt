package kekmech.ru.map

import kekmech.ru.common_analytics.Analytics
import kekmech.ru.common_analytics.AnalyticsImpl
import kekmech.ru.common_analytics.AnalyticsWrapper

class MapAnalytics(
    wrapper: AnalyticsWrapper
) : Analytics by AnalyticsImpl(wrapper, "Map")