package kekmech.ru.coreui.adapter

import android.util.Log
import java.util.*
import kotlin.collections.any

/**
 * Created by Kolomeytsev Anton on 09.07.2016.
 * This class is a part of Mr. Captain project.
 */
abstract class BaseClickableItem<T : BaseViewHolder>(
    private var itemId: String
): BaseItem<T>() {

    var clickListener: (String) -> Unit = {}

    override fun updateViewHolderNative(viewHolder: BaseViewHolder) {
        super.updateViewHolderNative(viewHolder)
        Log.d("VIEW HOLDER", "update")
        viewHolder.itemView.setOnClickListener {
            view -> view.postOnAnimation { clickListener(itemId) }
        }
    }
}