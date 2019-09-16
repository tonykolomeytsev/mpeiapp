package kekmech.ru.addscreen.presenter

import android.text.InputFilter
import android.text.Spanned
import kekmech.ru.addscreen.IAddFragment
import kekmech.ru.core.Presenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class AddFragmentPresenter @Inject constructor() : Presenter<IAddFragment> {
    private var htmlWorker: HTMLWorker? = null

    /**
     * subscribe to view events
     */
    override fun onResume(view: IAddFragment) {
        htmlWorker = HTMLWorker(view.web)
        view.onSearchClickListener = this::onSearch
    }

    /**
     * unsubscribe to view events
     */
    override fun onPause(view: IAddFragment) {
        htmlWorker = null
    }

    private fun onSearch(group: String) {
        GlobalScope.launch(Dispatchers.IO) {
            htmlWorker?.tryGroup(group)

        }
    }

    class PartialInputFormatter(private val pattern: Regex): InputFilter {

        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence {
            return ""
        }
    }


}