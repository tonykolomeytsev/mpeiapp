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

class ProfileItem(var academicScore: AcademicScore, val logoutListener: (View) -> Unit) : BaseItem<ProfileItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.apply {
            name = getFormattedName(academicScore.studentName)
            logout = logoutListener
            val adapter = BaseAdapter.Builder()
                .registerViewTypeFactory(ProfilePieceItem.Factory())
                .build()
            adapter.addItem(ProfilePieceItem("ГРУППА", academicScore.studentGroup))
            if (academicScore.studentQualification.isNotBlank())
                adapter.addItem(ProfilePieceItem("УРОВЕНЬ", academicScore.studentQualification))
            if (academicScore.studentSemester.isNotBlank())
                adapter.addItem(ProfilePieceItem("СЕМЕСТР", academicScore.studentSemester))
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


    /**
     * Для оптимизации главного экрана БАРСа
     */
    override fun equals(other: Any?) = if (other is ProfileItem) academicScore.profileInfo == other.academicScore.profileInfo else false
    override fun hashCode() = academicScore.profileInfo.hashCode()
    private val AcademicScore.profileInfo get() = "$studentName $studentGroup $studentQualification $studentSemester"
}