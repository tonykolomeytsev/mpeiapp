package kekmech.ru.coreui.items

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemErrorStateBinding
import kekmech.ru.ext_okhttp.NoInternetConnectionException
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import kekmech.ru.res_images.R.drawable as Images
import kekmech.ru.res_strings.R.string as Strings

public data class ErrorStateItem(val throwable: Throwable)

public class ErrorStateAdapterItem(
    onReloadClick: () -> Unit,
) : AdapterItem<ErrorStateViewHolder, ErrorStateItem>(
    isType = { it is ErrorStateItem },
    layoutRes = R.layout.item_error_state,
    viewHolderGenerator = ::ErrorStateViewHolder,
    itemBinder = ErrorStateItemBinder(onReloadClick)
)

public class ErrorStateViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemErrorStateBinding.bind(itemView)

    public fun setTitle(@StringRes titleResId: Int) {
        viewBinding.title.setText(titleResId)
    }

    public fun setDescription(@StringRes descriptionResId: Int, arg: String? = null) {
        val description = viewBinding.root.context.getString(descriptionResId, arg)
        viewBinding.description.text = description
    }

    public fun setIllustration(@DrawableRes illustrationResId: Int) {
        viewBinding.illustration.setImageResource(illustrationResId)
    }

    public fun setOnReloadClickListener(listener: () -> Unit) {
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
            ->
                Triple(
                    first = Strings.common_error_no_internet_title,
                    second = Strings.common_error_no_internet_description to null,
                    third = Images.ill_no_internet_error,
                )
            is HttpException ->
                Triple(
                    first = Strings.common_error_bad_response_title,
                    second = Strings.common_error_bad_response_description to throwable.code()
                        .toString(),
                    third = Images.ill_bad_response_error,
                )
                    .takeIf { throwable.code() in 300 until 600 }
                    ?: getErrorStateContent(null)
            is SSLHandshakeException ->
                Triple(
                    first = Strings.common_error_handshake_title,
                    second = Strings.common_error_handshake_description to null,
                    third = Images.ill_handshake_error,
                )
            else ->
                Triple(
                    first = Strings.common_error_view_title,
                    second = Strings.common_error_view_description to null,
                    third = Images.ill_view_error,
                )
        }
}
