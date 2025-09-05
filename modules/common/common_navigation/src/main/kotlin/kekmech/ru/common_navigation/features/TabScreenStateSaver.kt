package kekmech.ru.common_navigation.features

import android.os.Bundle
import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView

public interface TabScreenStateSaver  {

    public val stateBundle: Bundle

    public fun restoreBundle(commonBundle: Bundle)
    public fun updateBundle(commonBundle: Bundle)

    public fun saveState(recyclerView: RecyclerView)
    public fun restoreState(recyclerView: RecyclerView)
}

public class TabScreenStateSaverImpl(private val key: String) : TabScreenStateSaver {

    override var stateBundle: Bundle = Bundle()
        private set

    override fun restoreBundle(commonBundle: Bundle) {
        commonBundle.getBundle(key)?.let { stateBundle = it }
    }

    override fun updateBundle(commonBundle: Bundle) {
        commonBundle.putBundle(key, stateBundle)
    }

    override fun saveState(recyclerView: RecyclerView) {
        recyclerView.layoutManager?.let {
            stateBundle.putParcelable(recyclerView.key, it.onSaveInstanceState())
        }
    }

    override fun restoreState(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        val savedState = stateBundle.getParcelable<Parcelable>(recyclerView.key)
        if (layoutManager != null && savedState != null) {
            layoutManager.onRestoreInstanceState(savedState)
        }
    }
}

private val RecyclerView.key get() = id.toString()
