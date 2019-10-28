package kekmech.ru.coreui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Kolomeytsev Anton on 09.07.2016.
 * This class is a part of Mr. Captain project.
 */
abstract class BaseFactory(val layoutId: Int, val viewHolderGenerator: (View) -> BaseViewHolder? = { null }) {

    fun instanceNative(parent: ViewGroup?, inflater: LayoutInflater): BaseViewHolder {
        return instance(inflater.inflate(layoutId, parent, false))
    }

    open fun instance(view: View): BaseViewHolder = viewHolderGenerator(view)!!
}