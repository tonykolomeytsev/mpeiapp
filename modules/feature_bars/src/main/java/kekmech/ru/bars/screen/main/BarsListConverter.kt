package kekmech.ru.bars.screen.main

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import kekmech.ru.bars.R
import kekmech.ru.bars.items.UserNameHeaderItem
import kekmech.ru.bars.screen.main.BarsFragment.Companion.ITEM_DISCIPLINE_SHIMMER
import kekmech.ru.bars.screen.main.BarsFragment.Companion.ITEM_TEXT_SHIMMER
import kekmech.ru.bars.screen.main.elm.BarsState
import kekmech.ru.bars.screen.main.elm.FlowState
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.coreui.items.ShimmerItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.coreui.items.TextWithIconItem

internal class BarsListConverter(private val context: Context) {

    fun map(state: BarsState): List<Any> =
        when (state.flowState) {
            FlowState.LOGGED_IN -> mutableListOf<Any>().apply {
                addUserHeaderWithSettingsButton(state.userInfo?.name)
                addUserGroupLabel(state.userInfo?.group)
                addShowBarsInBrowserLabel()

                state.userInfo?.assessedDisciplines?.let { addAll(it) }
                    ?: addAll(List(3) { ShimmerItem(ITEM_DISCIPLINE_SHIMMER) })
            }
            else -> mutableListOf<Any>().apply {
                addUserHeaderWithSettingsButton(context.getString(R.string.bars_stub_student_name))
                addShowBarsInBrowserLabel()
            }
        }

    private fun MutableList<Any>.addUserHeaderWithSettingsButton(userName: String? = null) {
        add(UserNameHeaderItem(userName))
    }

    private fun MutableList<Any>.addUserGroupLabel(groupName: String? = null) {
        groupName?.let {
            add(
                TextWithIconItem(
                    itemId = 0,
                    text = SpannableStringBuilder()
                        .append("Группа ")
                        .append(
                            it,
                            ForegroundColorSpan(context.getThemeColor(R.attr.colorBlack)),
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        ),
                    drawableResID = R.drawable.ic_group_24,
                    tintColorAttrId = R.attr.colorGray70
                )
            )
        } ?: add(ShimmerItem(ITEM_TEXT_SHIMMER))
    }

    private fun MutableList<Any>.addShowBarsInBrowserLabel() {
        add(
            TextWithIconItem(
                itemId = 1,
                text = "Показать БАРС в браузере",
                drawableResID = R.drawable.ic_public_24px,
                tintColorAttrId = R.attr.colorMain,
                textStyleResId = R.style.H6_Main
            )
        )
        add(SpaceItem.VERTICAL_8)
    }
}