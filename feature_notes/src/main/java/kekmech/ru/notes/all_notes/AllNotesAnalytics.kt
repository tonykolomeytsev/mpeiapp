package kekmech.ru.notes.all_notes

import kekmech.ru.common_analytics.Analytics
import kekmech.ru.common_analytics.AnalyticsImpl
import kekmech.ru.common_analytics.AnalyticsWrapper

internal class AllNotesAnalytics(
    wrapper: AnalyticsWrapper
) : Analytics by AnalyticsImpl(wrapper, "AllNotes")