package kekmech.ru.addscreen

import android.webkit.WebView

interface IAddFragment {
    val web: WebView

    var onSearchClickListener: (String) -> Unit
}