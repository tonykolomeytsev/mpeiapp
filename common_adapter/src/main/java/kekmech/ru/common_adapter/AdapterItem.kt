package kekmech.ru.common_adapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

open class AdapterItem <in VH, in Model>(
    val isType: (Any) -> Boolean,
    @LayoutRes
    val layoutRes: Int,
    val viewHolderGenerator: (View) -> RecyclerView.ViewHolder,
    val itemBinder: BaseItemBinder<VH, Model>,
    val areItemsTheSame: (Model, Model) -> Boolean = { first, second -> first == second},
    val equals: (Model, Model) -> Boolean = { first, second -> first == second},
    val changePayload: (Model, Model) -> Any? = { oldItem, _ -> oldItem }
)