package kekmech.ru.feature_dashboard.items

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.getStringArray
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.domain_schedule.dto.SessionItem
import kekmech.ru.domain_schedule.dto.SessionItemType
import kekmech.ru.feature_dashboard.R
import kekmech.ru.feature_dashboard.databinding.ItemSessionBinding
import kekmech.ru.strings.StringArrays
import kekmech.ru.strings.Strings
import java.time.format.DateTimeFormatter

internal class SessionAdapterItem(context: Context) : AdapterItem<SessionViewHolder, SessionItem>(
    isType = { it is SessionItem },
    layoutRes = R.layout.item_session,
    viewHolderGenerator = ::SessionViewHolder,
    itemBinder = SessionItemBinder(context)
)

internal class SessionViewHolder(
    private val containerView: View
) : RecyclerView.ViewHolder(containerView) {

    private val viewBinding = ItemSessionBinding.bind(containerView)

    fun setName(text: String) {
        viewBinding.textViewName.text = text
    }

    fun setPerson(person: String) {
        viewBinding.textViewPerson.text = person
    }

    fun setPlace(place: String) {
        viewBinding.textViewPlace.text = place
    }

    fun setDateTime(text: String) {
        viewBinding.textViewDateTime.text = text
    }

    fun setTypeColor(@AttrRes colorRes: Int) {
        viewBinding.colorTag.backgroundTintList =
            ColorStateList.valueOf(containerView.context.getThemeColor(colorRes))
    }

    fun setTypeName(@StringRes typeRes: Int?) {
        if (typeRes != null) {
            viewBinding.textViewType.setText(typeRes)
        } else {
            viewBinding.textViewType.text = ""
        }
    }
}

private class SessionItemBinder(
    context: Context
) : BaseItemBinder<SessionViewHolder, SessionItem>() {

    private val colorTags by fastLazy {
        mapOf(
            SessionItemType.UNDEFINED to kekmech.ru.coreui.R.attr.colorGray30,
            SessionItemType.CONSULTATION to kekmech.ru.coreui.R.attr.colorGreen,
            SessionItemType.EXAM to kekmech.ru.coreui.R.attr.colorRed
        )
    }
    private val sessionDateTimeFormatter = SessionDateTimeFormatter(context)

    override fun bind(vh: SessionViewHolder, model: SessionItem, position: Int) {
        model.apply {
            vh.setName(model.name)
            vh.setPerson(model.person)
            vh.setPlace(model.place)
            vh.setTypeColor(colorTags.getValue(model.type))
            vh.setTypeName(when (model.type) {
                SessionItemType.EXAM -> Strings.dashboard_session_item_exam
                SessionItemType.CONSULTATION -> Strings.dashboard_session_item_consultation
                else -> null
            })
            vh.setDateTime(sessionDateTimeFormatter.format(model))
        }
    }
}

private class SessionDateTimeFormatter(
    context: Context
) {
    private val months = context.getStringArray(StringArrays.months)
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun format(sessionItem: SessionItem): String {
        val dayOfMonth = sessionItem.date.dayOfMonth
        val monthName = months[sessionItem.date.monthValue - 1]
        val time = sessionItem.time.start.format(timeFormatter)
        return "$dayOfMonth $monthName в $time"
    }
}