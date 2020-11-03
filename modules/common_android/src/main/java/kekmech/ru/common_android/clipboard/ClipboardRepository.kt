package kekmech.ru.common_android.clipboard

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Observable

class ClipboardRepository(private val context: Context) {

    fun observeTextClipboard(): Observable<String> = ClipboardUtils.observeClipboard(context)

    fun copyToClipboard(text: String) = Completable.fromCallable { ClipboardUtils.copyToClipboard(context, text) }
}