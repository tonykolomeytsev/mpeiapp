package kekmech.ru.common_mvi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import vivid.money.elmslie.android.base.ElmFragment

abstract class BaseFragment<Event : Any, Effect : Any, State : Any> :
    ElmFragment<Event, Effect, State>(),
    DisposableDelegate by DisposableDelegateImpl() {

    @LayoutRes
    protected open val layoutId: Int = 0

    protected val feature by lazy(LazyThreadSafetyMode.NONE) { store }

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

    override fun onDestroyView() {
        super.onDestroyView()
        clearDisposables()
    }

    protected fun Toolbar.init(@StringRes titleRes: Int? = null) =
        init(titleRes?.let { getString(it) })

    protected fun Toolbar.init(title: String?) {
        title?.let { setTitle(it) }
        setNavigationOnClickListener { requireActivity().onBackPressed() }
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