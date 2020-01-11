package kekmech.ru.coreui.adapter

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import kotlin.reflect.KProperty

/**
 * Created by Kolomeytsev Anton on 09.07.2016.
 * This class is a part of MPEIX project.
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseViewHolder2(itemView: View) : BaseViewHolder(itemView) {

    /**
     * Binds .text property of your view
     */
    fun bindText(id: Int) = TextViewDelegate(id)

    inner class TextViewDelegate(val id: Int) {
        var view: TextView? = null

        operator fun getValue(thisRef: BaseViewHolder2, property: KProperty<*>): String {
            if (view == null) view = itemView.findViewById(id)
            return view?.text?.toString() ?: ""
        }

        operator fun setValue(thisRef: BaseViewHolder2, property: KProperty<*>, value: String) {
            if (view == null) view = itemView.findViewById(id)
            view?.text = value
        }
    }

    /**
     * Binds setOnClickListener of your view
     */
    fun bindClickable(id: Int) = ClickableViewDelegate(id)

    inner class ClickableViewDelegate(val id: Int) {
        var view: View? = null
        private var clickListener: (View) -> Unit = {}

        operator fun getValue(thisRef: BaseViewHolder2, property: KProperty<*>): (View) -> Unit {
            if (view == null) view = itemView.findViewById(id)
            return clickListener
        }

        operator fun setValue(thisRef: BaseViewHolder2, property: KProperty<*>, value: (View) -> Unit) {
            if (view == null) view = itemView.findViewById(id)
            clickListener = value
            view?.setOnClickListener(value)
        }
    }

    /**
     * Binds visibility property. Set it to true for making your view visible and false for making invisible
     * @param keepBounds determines whether the view should still take up space in layout or not when it’s not visible
     */
    fun bindVisibility(id: Int, keepBounds: Boolean = false) = VisibleViewDelegate(id, keepBounds)

    inner class VisibleViewDelegate(val id: Int, private val keepBounds: Boolean) {
        var view: View? = null

        operator fun getValue(thisRef: BaseViewHolder2, property: KProperty<*>): Boolean {
            if (view == null) view = itemView.findViewById(id)
            return view?.visibility == View.VISIBLE
        }

        operator fun setValue(thisRef: BaseViewHolder2, property: KProperty<*>, value: Boolean) {
            if (view == null) view = itemView.findViewById(id)
            view?.visibility = when {
                value -> View.VISIBLE
                !value and keepBounds -> View.INVISIBLE
                else -> View.GONE
            }
        }
    }

    /**
     * Binds .progress property of ProgressBar. Uses float value from 0 to 1
     */
    fun bindProgress(id: Int) = ProgressViewDelegate(id)

    inner class ProgressViewDelegate(val id: Int) {
        var view: ProgressBar? = null

        operator fun getValue(thisRef: BaseViewHolder2, property: KProperty<*>): Float {
            if (view == null) view = itemView.findViewById(id)
            return if (view!!.max == 0) 0f else view!!.progress / view!!.max.toFloat()
        }

        operator fun setValue(thisRef: BaseViewHolder2, property: KProperty<*>, value: Float) {
            if (view == null) view = itemView.findViewById(id)
            view!!.progress = (value * view!!.max).toInt()
        }
    }

    fun bindEnabled(id: Int) = EnabledViewDelegate(id)

    inner class EnabledViewDelegate(val id: Int) {
        var view: View? = null

        operator fun getValue(thisRef: BaseViewHolder2, property: KProperty<*>): Boolean {
            if (view == null) view = itemView.findViewById(id)
            return view!!.isEnabled
        }

        operator fun setValue(thisRef: BaseViewHolder2, property: KProperty<*>, value: Boolean) {
            if (view == null) view = itemView.findViewById(id)
            view!!.isEnabled = value
        }
    }

}