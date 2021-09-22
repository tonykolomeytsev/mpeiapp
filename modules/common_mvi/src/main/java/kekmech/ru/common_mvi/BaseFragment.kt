package kekmech.ru.common_mvi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import kekmech.ru.common_android.hideKeyboard
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.screen.ElmDelegate

abstract class BaseFragment<Event : Any, Effect : Any, State : Any> :
    ElmFragment<Event, Effect, State>(),
    ElmDelegate<Event, Effect, State>,
    DisposableDelegate by DisposableDelegateImpl() {

    @LayoutRes
    protected open val layoutId: Int = 0

    protected val feature get() = store

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

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

                override fun onViewDetachedFromWindow(v: View?) = Unit
            })
        }
    }
}