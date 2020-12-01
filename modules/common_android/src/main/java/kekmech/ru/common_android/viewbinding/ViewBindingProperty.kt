package kekmech.ru.common_android.viewbinding

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal abstract class ViewBindingProperty<in T : Any, VB : ViewBinding>(
    private val viewBinder: (T) -> VB
) : ReadOnlyProperty<T, VB> {

    private var viewBinding: VB? = null
    private val lifecycleObserver = ClearOnDestroyLifecycleObserver()
    private var thisRef: T? = null

    protected abstract fun getLifecycleOwner(thisRef: T): LifecycleOwner

    @MainThread
    override fun getValue(thisRef: T, property: KProperty<*>): VB {
        viewBinding?.let { return it }

        this.thisRef = thisRef
        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            mainHandler.post { viewBinding = null }
        } else {
            lifecycle.addObserver(lifecycleObserver)
        }
        return viewBinder(thisRef).also { viewBinding = it }
    }

    @MainThread
    fun clear() {
        val thisRef = thisRef ?: return
        this.thisRef = null
        getLifecycleOwner(thisRef).lifecycle.removeObserver(lifecycleObserver)
        mainHandler.post { viewBinding = null }
    }

    private inner class ClearOnDestroyLifecycleObserver : DefaultLifecycleObserver {

        @MainThread
        override fun onDestroy(owner: LifecycleOwner): Unit = clear()
    }

    private companion object {
        private val mainHandler = Handler(Looper.getMainLooper())
    }
}