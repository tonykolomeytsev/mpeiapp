package kekmech.ru.feature_dashboard

import android.content.Context
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.feature_dashboard.items.BannerLunchItem
import kekmech.ru.feature_dashboard.items.SearchFieldItem
import kekmech.ru.feature_dashboard.presentation.DashboardState

class DashboardListConverter(
    context: Context
) {

    private val eventsHeader by fastLazy { SectionHeaderItem(
        title = context.getString(R.string.dashboard_section_header_events),
        itemId = DashboardFragment.SECTION_HEADER_EVENTS
    ) }

    private val notesHeader by fastLazy { SectionHeaderItem(
        title = context.getString(R.string.dashboard_section_header_notes),
        itemId = DashboardFragment.SECTION_HEADER_NOTES
    ) }

    fun map(state: DashboardState): List<Any> {

        return mutableListOf<Any>().apply {
            add(SpaceItem.VERTICAL_24)
            add(SearchFieldItem)
            add(SpaceItem.VERTICAL_24)
            add(BannerLunchItem)
            add(SpaceItem.VERTICAL_24)
            add(eventsHeader)
            add(SpaceItem.VERTICAL_12)
            addAll(state.todayClasses)
        }
    }
}