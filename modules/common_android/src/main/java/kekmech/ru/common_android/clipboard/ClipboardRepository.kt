package kekmech.ru.common_android.clipboard

import android.content.Context
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class ClipboardRepository(private val context: Context) {

    fun observeTextClipboard(): Observable<String> = ClipboardUtils.observeClipboard(context)

    fun copyToClipboard(text: String): Completable =
        Completable.fromCallable { ClipboardUtils.copyToClipboard(context, text) }
}