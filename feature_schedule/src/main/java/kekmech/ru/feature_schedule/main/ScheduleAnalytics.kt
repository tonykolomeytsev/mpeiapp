package kekmech.ru.feature_schedule.main

import kekmech.ru.common_analytics.Analytics
import kekmech.ru.common_analytics.AnalyticsImpl
import kekmech.ru.common_analytics.AnalyticsWrapper

class ScheduleAnalytics(
    wrapper: AnalyticsWrapper
) : Analytics by AnalyticsImpl(wrapper, "Schedule")