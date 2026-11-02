package kekmech.ru.lib_navigation.features

import android.os.Bundle
import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.ext_android.parcelable

/**
 * Сохраняет состояние экрана-таба (позиции списков и т.п.) между переключениями табов.
 *
 * При переключении таба back stack заменяется целиком и фрагмент таба уничтожается,
 * поэтому состояние хранится в [TabStateStore], который живет вместе с процессом.
 */
public interface TabScreenStateSaver {

    public val stateBundle: Bundle

    public fun saveState(recyclerView: RecyclerView)
    public fun restoreState(recyclerView: RecyclerView)
}

public class TabScreenStateSaverImpl(private val key: String) : TabScreenStateSaver {

    override val stateBundle: Bundle
        get() = TabStateStore.bundleFor(key)

    override fun saveState(recyclerView: RecyclerView) {
        recyclerView.layoutManager?.let {
            stateBundle.putParcelable(recyclerView.key, it.onSaveInstanceState())
        }
    }

    override fun restoreState(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        val savedState: Parcelable? = stateBundle.parcelable(recyclerView.key)
        if (layoutManager != null && savedState != null) {
            layoutManager.onRestoreInstanceState(savedState)
        }
    }
}

internal object TabStateStore {

    private val bundles = mutableMapOf<String, Bundle>()

    fun bundleFor(key: String): Bundle = bundles.getOrPut(key) { Bundle() }
}

private val RecyclerView.key get() = id.toString()
