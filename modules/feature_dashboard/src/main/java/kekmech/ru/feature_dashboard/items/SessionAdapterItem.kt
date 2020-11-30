package kekmech.ru.feature_dashboard.items

import android.view.View
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.domain_schedule.dto.SessionItem
import kekmech.ru.feature_dashboard.R
import kotlinx.android.extensions.LayoutContainer
import java.time.format.DateTimeFormatter

interface SessionViewHolder {
    fun setName(text: String)
    fun setTypeName(type: String)
    fun setTypeColor(@ColorRes colorRes: Int)
    fun setPlace(place: String)
    fun setPerson(person: String)
    fun setTimeStart(text: String)
}

private class SessionViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), SessionViewHolder, LayoutContainer {

    override fun setName(text: String) {
        TODO("Not yet implemented")
    }

    override fun setPerson(person: String) {
        TODO("Not yet implemented")
    }

    override fun setPlace(place: String) {
        TODO("Not yet implemented")
    }

    override fun setTimeStart(text: String) {
        TODO("Not yet implemented")
    }

    override fun setTypeColor(colorRes: Int) {
        TODO("Not yet implemented")
    }

    override fun setTypeName(type: String) {
        TODO("Not yet implemented")
    }
}

class SessionItemBinder : BaseItemBinder<SessionViewHolder, SessionItem>() {

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun bind(vh: SessionViewHolder, model: SessionItem, position: Int) {
        model.apply {
            vh.setName(model.name)
            vh.setPerson(model.person)
            vh.setPlace(model.place)
            vh.setTimeStart(model.time.start.format(timeFormatter))
            vh.setTypeColor(R.color.colorGreen)
            vh.setTypeName(model.type.name)
        }
    }
}

class SessionAdapterItem : AdapterItem<SessionViewHolder, SessionItem>(
    isType = { it is SessionItem },
    layoutRes = 0,
    viewHolderGenerator = ::SessionViewHolderImpl,
    itemBinder = SessionItemBinder()
)