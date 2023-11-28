package kekmech.ru.feature_bars_impl.presentation.screen.main

import android.content.Context
import android.os.Build
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import kekmech.ru.coreui.items.EmptyStateItem
import kekmech.ru.coreui.items.ShimmerItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.coreui.items.TextWithIconItem
import kekmech.ru.ext_android.getThemeColor
import kekmech.ru.feature_bars_impl.R
import kekmech.ru.feature_bars_impl.presentation.items.LoginToBarsItem
import kekmech.ru.feature_bars_impl.presentation.items.UserNameHeaderItem
import kekmech.ru.feature_bars_impl.presentation.screen.main.BarsFragment.Companion.ITEM_BROWSER_LABEL
import kekmech.ru.feature_bars_impl.presentation.screen.main.BarsFragment.Companion.ITEM_BYPASSING_LABEL
import kekmech.ru.feature_bars_impl.presentation.screen.main.BarsFragment.Companion.ITEM_GROUP_LABEL
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsState
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.FlowState
import kekmech.ru.coreui.R as coreui_R
import kekmech.ru.res_icons.R.drawable as Icons
import kekmech.ru.res_strings.R.string as Strings

internal class BarsListConverter(private val context: Context) {

    private val disciplineShimmersItem =
        List(DISCIPLINE_SHIMMER_COUNT) { ShimmerItem(R.layout.item_discipline_shimmer) }
    private val textShimmerItem = ShimmerItem(coreui_R.layout.item_text_shimmer)
    private val loginShimmerItem = ShimmerItem(R.layout.item_login_to_bars_shimmer)

    @Suppress("NestedBlockDepth")
    fun map(state: BarsState): List<Any> =
        when {
            state.flowState == FlowState.LOGGED_IN ->
                mutableListOf<Any>().apply {
                    addUserHeaderWithSettingsButton(state.userInfo?.name)
                    addUserGroupLabel(state.userInfo?.group)
                    addShowBarsInBrowserLabel()
                    maybeAddBypassingLabel()
                    add(SpaceItem.VERTICAL_8)

                    if (!state.isAfterErrorLoadingConfig) {
                        state.userInfo?.assessedDisciplines?.let { disciplines ->
                            if (disciplines.isEmpty()) {
                                addEmptyDisciplinesItem()
                            } else {
                                addAll(disciplines)
                            }
                        } ?: addAll(disciplineShimmersItem)
                    } else {
                        addErrorStateItem(state)
                    }
                }
            state.flowState == FlowState.NOT_LOGGED_IN && state.config != null ->
                mutableListOf<Any>().apply {
                    addUserHeaderWithSettingsButton(context.getString(Strings.bars_stub_student_name))
                    addShowBarsInBrowserLabel()
                    maybeAddBypassingLabel()
                    add(SpaceItem.VERTICAL_8)
                    add(LoginToBarsItem)
                }
            else ->
                mutableListOf<Any>().apply {
                    addUserHeaderWithSettingsButton(context.getString(Strings.bars_stub_student_name))
                    addShowBarsInBrowserLabel()
                    maybeAddBypassingLabel()
                    add(SpaceItem.VERTICAL_8)
                    addErrorStateItem(state)
                }
        }

    private fun MutableList<Any>.addUserHeaderWithSettingsButton(userName: String? = null) {
        add(UserNameHeaderItem(userName))
    }

    private fun MutableList<Any>.addUserGroupLabel(groupName: String? = null) {
        groupName?.let {
            add(
                TextWithIconItem(
                    itemId = ITEM_GROUP_LABEL,
                    text = SpannableStringBuilder()
                        .append(context.getString(Strings.bars_stub_student_group))
                        .append(" ")
                        .append(
                            it,
                            ForegroundColorSpan(context.getThemeColor(coreui_R.attr.colorBlack)),
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        ),
                    drawableResID = Icons.ic_groups_black_24,
                    tintColorAttrId = coreui_R.attr.colorGray70
                )
            )
        } ?: add(textShimmerItem)
    }

    private fun MutableList<Any>.addShowBarsInBrowserLabel() {
        add(
            TextWithIconItem(
                itemId = ITEM_BROWSER_LABEL,
                textResId = Strings.bars_stub_show_browser_label,
                drawableResID = R.drawable.ic_public_24px,
                tintColorAttrId = coreui_R.attr.colorActive,
                textStyleResId = coreui_R.style.H6_Main
            )
        )
    }

    private fun MutableList<Any>.maybeAddBypassingLabel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(
                TextWithIconItem(
                    itemId = ITEM_BYPASSING_LABEL,
                    textResId = Strings.bars_bypassing_item,
                    drawableResID = R.drawable.ic_warning24,
                    tintColorAttrId = coreui_R.attr.colorActive,
                    textStyleResId = coreui_R.style.H6_Main,
                )
            )
        }
    }

    private fun MutableList<Any>.addEmptyDisciplinesItem() {
        add(
            EmptyStateItem(
                titleRes = Strings.bars_stub_empty_disciplines_header,
                subtitleRes = Strings.bars_stub_empty_disciplines_description
            )
        )
    }

    private fun MutableList<Any>.addErrorStateItem(state: BarsState) {
        if (!state.isAfterErrorLoadingConfig) {
            add(loginShimmerItem)
        } else {
            add(
                EmptyStateItem(
                    titleRes = Strings.bars_stub_error_loading_config_header,
                    subtitleRes = Strings.bars_stub_error_loading_config_description
                )
            )
        }
    }

    private companion object {

        const val DISCIPLINE_SHIMMER_COUNT = 3
    }
}
