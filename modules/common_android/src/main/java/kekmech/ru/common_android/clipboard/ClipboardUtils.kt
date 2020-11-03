package kekmech.ru.common_android.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import io.reactivex.Observable
import io.reactivex.disposables.Disposables

object ClipboardUtils {

    fun copyToClipboard(context: Context, text: String, label: String = "") {
        val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.setPrimaryClip(ClipData.newPlainText(label, text))
    }

    fun observeClipboard(context: Context): Observable<String> = Observable.create { subscriber ->
        val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.let { _ ->
            val invalidate = {
                clipboard.primaryClip?.let { if (it.itemCount > 0) it.getItemAt(0).text else null }
                    ?.let { subscriber.onNext(it.toString()) }
            }
            invalidate()

            val listener = ClipboardManager.OnPrimaryClipChangedListener { invalidate() }
            clipboard.addPrimaryClipChangedListener(listener)
            subscriber.setDisposable(Disposables.fromAction { clipboard.removePrimaryClipChangedListener(listener) })
        }
    }
}