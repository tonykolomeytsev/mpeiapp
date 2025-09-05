package kekmech.ru.common_adapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

@Suppress("LongParameterList")
public open class AdapterItem <in VH, in Model>(
    public val isType: (Any) -> Boolean,
    @field:LayoutRes
    public val layoutRes: Int,
    public val viewHolderGenerator: (View) -> RecyclerView.ViewHolder,
    public val itemBinder: BaseItemBinder<VH, Model>,
    public val areItemsTheSame: (Model, Model) -> Boolean = { first, second -> first == second},
    public val equals: (Model, Model) -> Boolean = { first, second -> first == second},
    public val changePayload: (Model, Model) -> Any? = { oldItem, _ -> oldItem }
)
