package kekmech.ru.bars.main.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder2
import kotlinx.coroutines.suspendAtomicCancellableCoroutine

class ProfileItem(val academicScore: AcademicScore, val logoutListener: (View) -> Unit) : BaseItem<ProfileItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.apply {
            name = getFormattedName(academicScore.studentName)
            logout = logoutListener
            val adapter = BaseAdapter.Builder()
                .registerViewTypeFactory(ProfilePieceItem.Factory())
                .build()
            adapter.addItem(ProfilePieceItem("ГРУППА", academicScore.studentGroup))
            adapter.addItem(ProfilePieceItem("ПИСОС", "50 см"))
            adapter.addItem(ProfilePieceItem("КУМИР", "ЗУЕВ"))
            recycler.layoutManager = LinearLayoutManager(viewHolder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recycler.adapter = adapter
        }
    }

    private fun getFormattedName(name: String): String {
        val fio = name.split(' ')
        if (fio.size == 3) {
            return "${fio[0]} ${fio[1]}"
        } else return name
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder2(view) {
        var name by bindText(R.id.textViewBarsName)
        var logout by bindClickable(R.id.buttonBarsLogout)
        var recycler by bind<RecyclerView>(R.id.recyclerView)
    }

    class Factory : BaseFactory(R.layout.item_profile, ::ViewHolder)
}