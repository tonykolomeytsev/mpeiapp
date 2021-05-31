package kekmech.ru.bars.screen.main

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import kekmech.ru.bars.R
import kekmech.ru.bars.items.MenusItem
import kekmech.ru.bars.items.UserNameHeaderItem
import kekmech.ru.bars.screen.main.elm.BarsState
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.coreui.items.TextWithIconItem

internal class BarsListConverter(private val context: Context) {

    private val listStub = listOf<Any>(MenusItem)

    fun map(state: BarsState): List<Any> = when {
        state.config != null && state.userBars != null -> mutableListOf<Any>().apply {
            state.userBars.name?.let {
                add(UserNameHeaderItem(it))
            } ?: run {
                //shimmer
            }
            add(SpaceItem.VERTICAL_8)

            state.userBars.group?.let {
                add(TextWithIconItem(
                    itemId = 0,
                    text = SpannableStringBuilder()
                        .append("Группа ")
                        .append(it, ForegroundColorSpan(context.getThemeColor(R.attr.colorBlack)), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE),
                    drawableResID = R.drawable.ic_group_24,
                    tintColorAttrId = R.attr.colorGray70
                ))
            }
            add(TextWithIconItem(
                itemId = 1,
                text = "Показать БАРС в браузере",
                drawableResID = R.drawable.ic_public_24px,
                tintColorAttrId = R.attr.colorMain,
                textStyleResId = R.style.H6_Main
            ))

            add(SpaceItem.VERTICAL_8)
            add(MenusItem)

            add(SpaceItem.VERTICAL_12)

            addAll(state.userBars.assessedDisciplines.orEmpty())
        }
        state.config == null && state.userBars == null -> when {
            state.isAfterErrorLoadingConfig -> emptyList()
            else -> emptyList()
        }
        state.config != null && state.userBars == null -> when {
            state.isAfterErrorLoadingUserBars -> emptyList()
            else -> emptyList()
        }
        else -> listStub
    }
}