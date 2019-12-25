package kekmech.ru.settings.adapter

import android.view.View
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import kekmech.ru.coreui.adapter.*
import kekmech.ru.settings.R

class SingleLineTumblerItem(
    val string: String
) : BaseItem<SingleLineTumblerItem.ViewHolder>() {

    private val nopes = listOf("Nope", "No", "Nooo", "Не", "Нет", "Неа", "Нiт", "Ноу")

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.textView.text = string
        viewHolder.tumbler.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewHolder.tumbler.post { viewHolder.tumbler.isChecked = false }

            }
            Toast.makeText(viewHolder.itemView.context, nopes.random(), Toast.LENGTH_SHORT).show()
        }
        viewHolder.root.setOnClickListener {
            Toast.makeText(viewHolder.itemView.context, nopes.random(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val root by bind<FrameLayout>(R.id.rootView)
        val textView by bind<TextView>(R.id.textViewPrimary)
        val tumbler by bind<Switch>(R.id.theSwitch)
        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_single_line_tumbler) {
        override fun instance(view: View) =
            ViewHolder(view)
    }

    companion object {
        fun buildAdapter() = BaseAdapter.Builder().registerViewTypeFactory(Factory()).build()
    }
}