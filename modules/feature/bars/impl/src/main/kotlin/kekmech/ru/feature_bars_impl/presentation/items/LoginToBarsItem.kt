package kekmech.ru.feature_bars_impl.presentation.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.feature_bars_impl.R
import kekmech.ru.feature_bars_impl.databinding.ItemLoginToBarsBinding
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder

internal object LoginToBarsItem

internal class LoginToBarsAdapterItem(
    onLoginClick: () -> Unit
) : AdapterItem<LoginToBarsViewHolder, LoginToBarsItem>(
    isType = { it is LoginToBarsItem },
    layoutRes = R.layout.item_login_to_bars,
    viewHolderGenerator = ::LoginToBarsViewHolder,
    itemBinder = object : BaseItemBinder<LoginToBarsViewHolder, LoginToBarsItem>() {

        override fun bind(vh: LoginToBarsViewHolder, model: LoginToBarsItem, position: Int) {
            vh.setOnLoginClickListener(onLoginClick)
        }
    },
    areItemsTheSame = { _, _ -> true }
)

internal class LoginToBarsViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemLoginToBarsBinding.bind(itemView)

    fun setOnLoginClickListener(listener: () -> Unit) {
        viewBinding.login.setOnClickListener { listener.invoke() }
    }
}
