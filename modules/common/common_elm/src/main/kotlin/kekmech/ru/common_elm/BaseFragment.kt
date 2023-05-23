package kekmech.ru.common_elm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kekmech.ru.common_android.hideKeyboard
import vivid.money.elmslie.android.elmStore
import vivid.money.elmslie.android.renderer.ElmRenderer
import vivid.money.elmslie.android.renderer.ElmRendererDelegate
import vivid.money.elmslie.core.store.Store

abstract class BaseFragment<Event : Any, Effect : Any, State : Any>(
    @LayoutRes val layoutResId: Int,
) :
    Fragment(layoutResId),
    ElmRendererDelegate<Effect, State>,
    DisposableDelegate by DisposableDelegateImpl() {

    init {
        @Suppress("LeakingThis")
        ElmRenderer(this, lifecycle)
    }

    override val store: Store<Event, Effect, State> by elmStore { createStore() }

    abstract fun createStore(): Store<Event, Effect, State>

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutResId, container, false)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.requestApplyInsetsWhenAttached()
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearDisposables()
    }

    private fun View.requestApplyInsetsWhenAttached() {
        if (isAttachedToWindow) {
            requestApplyInsets()
        } else {
            addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    v.removeOnAttachStateChangeListener(this)
                    v.requestApplyInsets()
                }

                override fun onViewDetachedFromWindow(v: View) = Unit
            })
        }
    }
}
