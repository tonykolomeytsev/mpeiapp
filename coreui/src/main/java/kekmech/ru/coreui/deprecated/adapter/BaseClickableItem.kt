package kekmech.ru.coreui.deprecated.adapter

import android.util.Log

/**
 * Created by Kolomeytsev Anton on 09.07.2016.
 * This class is a part of Mr. Captain project.
 */
abstract class BaseClickableItem<T : BaseViewHolder>: BaseItem<T>() {

    var clickListener: (BaseClickableItem<T>) -> Unit = {}

    override fun updateViewHolderNative(viewHolder: BaseViewHolder) {
        super.updateViewHolderNative(viewHolder)
        Log.d("VIEW HOLDER", "update")
        viewHolder.itemView.setOnClickListener {
                view -> view.postOnAnimation { clickListener(this) }
        }
    }
}