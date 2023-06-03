package kekmech.ru.common_android.viewbinding

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import timber.log.Timber
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class ViewBindingProperty<in F : Fragment, VB : ViewBinding>(
    private val viewBinder: (F) -> VB,
) : ReadOnlyProperty<F, VB> {

    private var viewBinding: VB? = null

    @MainThread
    override fun getValue(thisRef: F, property: KProperty<*>): VB {
        viewBinding?.let { return it }

        val lifecycle = thisRef.viewLifecycleOwner.lifecycle
        val viewBinding = viewBinder(thisRef)

        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            Timber.w("Access to viewBinding after Lifecycle is destroyed or hasn't created yet.")
        } else {
            lifecycle.addObserver(ClearOnDestroyLifecycleObserver(this))
            this.viewBinding = viewBinding
        }
        return viewBinding
    }

    @MainThread
    fun clear() {
        viewBinding = null
    }

    private class ClearOnDestroyLifecycleObserver(
        private val property: ViewBindingProperty<*, *>,
    ) : DefaultLifecycleObserver {

        @MainThread
        override fun onDestroy(owner: LifecycleOwner) {
            if (!mainHandler.post { property.clear() }) {
                property.clear()
            }
        }
    }

    private companion object {

        private val mainHandler = Handler(Looper.getMainLooper())
    }
}
