package kekmech.ru.coreui.adapter2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class BaseAdapter2(
    private val setOfTypes: HashSet<KClass<BaseItem2<*>>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val items = mutableListOf<BaseItem2<*>>()
    private val mapOfFactories = mutableMapOf<Int, Pair<Int, (View) -> RecyclerView.ViewHolder>>()

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        val itemIndex = setOfTypes.indexOf(items[position]::class)
        if (itemIndex != -1) {
            if (!mapOfFactories.containsKey(itemIndex)) {
                val item = items[position]
                val constructor = item.viewHolderClass.java.getConstructor(View::class.java)
                mapOfFactories[itemIndex] = item.layoutId to { v: View -> constructor.newInstance(v) }
            }
            return itemIndex
        }
        else throw RuntimeException("Тип ${items[position]::class} не зарегистрирован в этом адаптере!")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val (layoutId, constructor) = mapOfFactories[viewType]!!
        val view = LayoutInflater.from(parent.context!!)
            .inflate(layoutId, parent, false)
        return constructor(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items[position].updateViewHolderNative(holder)
    }

    class Builder {
        private val listOfTypes = mutableListOf<KClass<BaseItem2<*>>>()

        fun registerItemTypes(vararg kClass: KClass<out BaseItem2<*>>): Builder {
            listOfTypes.addAll(kClass.map { it as KClass<BaseItem2<*>>})
            return this
        }

        fun build() = BaseAdapter2(listOfTypes.toHashSet())
    }
}