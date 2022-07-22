package kekmech.ru.coreui.items

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.exceptions.CompositeException
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_cache.core.CacheIsEmptyException
import kekmech.ru.common_network.okhttp.NoInternetConnectionException
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemErrorStateBinding
import kekmech.ru.strings.Strings
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

data class ErrorStateItem(val throwable: Throwable)

class ErrorStateAdapterItem(
    onReloadClick: () -> Unit,
) : AdapterItem<ErrorStateViewHolder, ErrorStateItem>(
    isType = { it is ErrorStateItem },
    layoutRes = R.layout.item_error_state,
    viewHolderGenerator = ::ErrorStateViewHolder,
    itemBinder = ErrorStateItemBinder(onReloadClick)
)

class ErrorStateViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemErrorStateBinding.bind(itemView)

    fun setTitle(@StringRes titleResId: Int) {
        viewBinding.title.setText(titleResId)
    }

    fun setDescription(@StringRes descriptionResId: Int, arg: String? = null) {
        val description = viewBinding.root.context.getString(descriptionResId, arg)
        viewBinding.description.text = description
    }

    fun setIllustration(@DrawableRes illustrationResId: Int) {
        viewBinding.illustration.setImageResource(illustrationResId)
    }

    fun setOnReloadClickListener(listener: () -> Unit) {
        viewBinding.reload.setOnClickListener { listener.invoke() }
    }
}

private class ErrorStateItemBinder(
    private val onReloadClick: () -> Unit,
) : BaseItemBinder<ErrorStateViewHolder, ErrorStateItem>() {

    override fun bind(vh: ErrorStateViewHolder, model: ErrorStateItem, position: Int) {
        val (titleResId, description, illustrationResId) = getErrorStateContent(model.throwable)
        with(vh) {
            setTitle(titleResId)
            setDescription(description.first, description.second)
            setIllustration(illustrationResId)
            setOnReloadClickListener(onReloadClick)
        }
    }

    @Suppress("MagicNumber")
    private fun getErrorStateContent(throwable: Throwable?): Triple<Int, Pair<Int, String?>, Int> =
        when (throwable) {
            is NoInternetConnectionException,
            is UnknownHostException,
            is CacheIsEmptyException,
            ->
                Triple(
                    first = Strings.common_error_no_internet_title,
                    second = Strings.common_error_no_internet_description to null,
                    third = R.drawable.ill_no_internet_error
                )
            is HttpException ->
                Triple(
                    first = Strings.common_error_bad_response_title,
                    second = Strings.common_error_bad_response_description to throwable.code().toString(),
                    third = R.drawable.ill_bad_response_error
                )
                    .takeIf { throwable.code() in 300 until 600 }
                    ?: getErrorStateContent(null)
            is SSLHandshakeException ->
                Triple(
                    first = Strings.common_error_handshake_title,
                    second = Strings.common_error_handshake_description to null,
                    third = R.drawable.ill_handshake_error
                )
            is CompositeException ->
                getErrorStateContent(throwable.exceptions.firstOrNull())
            else ->
                Triple(
                    first = Strings.common_error_view_title,
                    second = Strings.common_error_view_description to null,
                    third = R.drawable.ill_view_error
                )
        }
}