package kekmech.ru.coreui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import java.util.*
import java.util.zip.Inflater

/**
 * Created by Kolomeytsev Anton on 09.07.2016.
 * This class is a part of Mr. Captain project.
 */
open class BaseAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    internal val viewFactories = hashMapOf<Int, BaseFactory>()
    val baseItems: MutableList<BaseItem<*>> = ArrayList()
    val items get() = baseItems // для сокращения кода
    var inflater: LayoutInflater? = null

    override fun getItemViewType(position: Int): Int {
        return baseItems[position].getType(viewFactories)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        baseItems[position].updateViewHolderNative(holder)
    }

    override fun getItemCount() = baseItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        /*if (inflater == null) */inflater = LayoutInflater.from(parent.context)
        return viewFactories[viewType]!!.instanceNative(parent, inflater!!)
    }

    /**
     * Добавляет элемент в позицию [index] если baseItems.size < [index].
     * В противном случае, добавляет элемент в конец.
     * В обоих случаях запустит notifyItemInserted с нужным индексом.
     */
    open fun addItem(index: Int, baseItem: BaseItem<*>) {
        baseItems.add(if (index < itemCount) index else itemCount, baseItem)
        notifyItemInserted(baseItems.indexOf(baseItem))
    }

    open fun addItem(baseItem: BaseItem<*>) {
        baseItems.add(baseItem)
        notifyItemInserted(baseItems.indexOf(baseItem))
    }

    open fun getDiffResult(newListOfItems: List<BaseItem<*>>): DiffUtil.DiffResult {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = items.size
            override fun getNewListSize() = newListOfItems.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                items[oldItemPosition].javaClass == newListOfItems[newItemPosition].javaClass
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                items[oldItemPosition] == newListOfItems[newItemPosition]
        }
        return DiffUtil.calculateDiff(diffCallback)
    }

    class Builder {
        private val adapter = BaseAdapter()

        fun registerViewTypeFactory(factory: BaseFactory): Builder {
            adapter.viewFactories += adapter.viewFactories.size to factory
            return this
        }

        fun build() = adapter
    }

}