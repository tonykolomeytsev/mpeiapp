package kekmech.ru.notes.note_list

import kekmech.ru.common_analytics.Analytics
import kekmech.ru.common_analytics.AnalyticsImpl
import kekmech.ru.common_analytics.AnalyticsWrapper

internal class NoteListAnalytics(
    wrapper: AnalyticsWrapper
) : Analytics by AnalyticsImpl(wrapper, "NoteList")