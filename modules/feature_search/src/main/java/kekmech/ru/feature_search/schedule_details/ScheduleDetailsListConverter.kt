package kekmech.ru.feature_search.schedule_details

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.annotation.StringRes
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_schedule.items.DayItem
import kekmech.ru.common_schedule.utils.TimeUtils
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_search.R
import kekmech.ru.feature_search.item.ButtonItem
import kekmech.ru.feature_search.item.WeekMinItem
import kekmech.ru.feature_search.schedule_details.mvi.ScheduleDetailsState

internal object ScheduleDetailsListConverter {

    private val classesEmptyStateItem = EmptyStateItem(

    )

    private fun getFavoritesItem(isInFavorites: Boolean?): Any? {
        if (isInFavorites == null) return null
        val (stringId, drawableId) = if (!isInFavorites){
            R.string.search_schedule_details_add_favorites to R.drawable.ic_star_outline_black_18dp
        } else {
            R.string.search_schedule_details_remove_favorites to R.drawable.ic_star_black_18dp
        }
        return TextWithIconItem(
            itemId = ITEM_FAVORITES,
            textResId = stringId,
            drawableResID = drawableId,
            tintColorAttrId = R.attr.colorMain,
            textStyleResId = R.style.H6_Main
        )
    }

    fun map(state: ScheduleDetailsState, context: Context): List<Any> {
        return mutableListOf<Any>().apply {
            add(PullItem)
            add(TextItem(text = state.searchResult.name, styleResId = R.style.H2))
            add(SpaceItem.VERTICAL_12)
            add(TextItem(text = state.searchResult.description, styleResId = R.style.H6_Gray70))
            add(SpaceItem.VERTICAL_8)

            getFavoritesItem(state.isInFavorites)?.let {
                add(it)
            } ?: add(ShimmerItem(ITEM_TEXT_SHIMMER_ID))

            getDescriptionItem(state, context)?.let {
                add(it)
            } ?: add(ShimmerItem(ITEM_TEXT_SHIMMER_ID))

            add(SpaceItem.VERTICAL_8)

            getWeekItem(state)?.let {
                add(it)
            } ?: add(ShimmerItem(ITEM_WEEK_SHIMMER_ID))
            
            add(SpaceItem.VERTICAL_8)

            if (state.thisWeek != null && state.nextWeek != null) {
                (state.thisWeek + state.nextWeek)
                    .find { it.date == state.selectedDayDate }
                    ?.classes
                    ?.onEach { it.scheduleType = state.scheduleType }
                    ?.let { addAll(it) }
                    ?: add(classesEmptyStateItem)
            } else {
                add(ShimmerItem(ITEM_CLASSES_SHIMMER_ID))
            }

            add(SpaceItem.VERTICAL_8)

            add(ButtonItem(ITEM_BUTTON_SWITCH, R.string.search_schedule_details_switch_schedule))
        }
    }

    private fun getDescriptionItem(state: ScheduleDetailsState, context: Context): Any? {
        if (state.isLoadingSchedule) return null
        val thisWeekDescription: CharSequence = getScheduleDescription(
            classesCount = state.thisWeek?.sumBy { it.classes.size } ?: 0,
            weekDescription = R.string.search_schedule_details_this_week_description,
            emptyWeekDescription = R.string.search_schedule_details_this_week_description_empty,
            context = context
        )
        val nextWeekDescription: CharSequence = getScheduleDescription(
            classesCount = state.thisWeek?.sumBy { it.classes.size } ?: 0,
            weekDescription = R.string.search_schedule_details_next_week_description,
            emptyWeekDescription = R.string.search_schedule_details_next_week_description_empty,
            context = context
        )
        val builder = SpannableStringBuilder()
        builder.append(thisWeekDescription)
        builder.append(", ")
        builder.append(nextWeekDescription)
        return TextWithIconItem(
            text = builder,
            textStyleResId = R.style.H7,
            itemId = 2,
            drawableResID = R.drawable.ic_navigation_schedule,
            tintColorAttrId = R.attr.colorGray70
        )
    }

    private fun getScheduleDescription(
        classesCount: Int,
        @StringRes weekDescription: Int,
        @StringRes emptyWeekDescription: Int,
        context: Context,
    ): CharSequence {
        return if (classesCount > 0) {
            val adoptedNumber = adoptNumber(classesCount, context)
            val fullString = context.getString(weekDescription, adoptedNumber)
            SpannableString.valueOf(fullString).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, adoptedNumber.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                setSpan(ForegroundColorSpan(context.getThemeColor(R.attr.colorBlack)), 0, adoptedNumber.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }
        } else {
            context.getString(emptyWeekDescription)
        }
    }

    private fun getWeekItem(state: ScheduleDetailsState): Any? {
        state.thisWeek ?: return null
        state.nextWeek ?: return null
        val dayItems =
            state.thisWeek.map { DayItem(it.date, 0, it.date == state.selectedDayDate) } +
                    state.nextWeek.map { DayItem(it.date, 0, it.date == state.selectedDayDate) }
        return WeekMinItem(dayItems)
    }

    private fun adoptNumber(
        num: Int,
        context: Context
    ): String = "$num " + when {
        num in 11..19 -> context.getString(R.string.search_schedule_details_classes_5_0)
        num % 10 == 1 -> context.getString(R.string.search_schedule_details_classes_1)
        num % 10 in 2..4 -> context.getString(R.string.search_schedule_details_classes_2_4)
        else -> context.getString(R.string.search_schedule_details_classes_5_0)
    }
}