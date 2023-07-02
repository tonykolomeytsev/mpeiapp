package kekmech.ru.library_elm

import androidx.annotation.LayoutRes
import kekmech.ru.ext_android.fragment.BottomSheetDialogFragment
import vivid.money.elmslie.android.elmStore
import vivid.money.elmslie.android.renderer.ElmRenderer
import vivid.money.elmslie.android.renderer.ElmRendererDelegate
import vivid.money.elmslie.core.store.Store

abstract class BaseBottomSheetDialogFragment<Event : Any, Effect : Any, State : Any>(@LayoutRes layoutResId: Int) :
    BottomSheetDialogFragment(layoutResId),
    ElmRendererDelegate<Effect, State>,
    DisposableDelegate by DisposableDelegateImpl() {

    init {
        @Suppress("LeakingThis")
        ElmRenderer(this, lifecycle)
    }

    override val store: Store<Event, Effect, State> by elmStore { createStore() }

    abstract fun createStore(): Store<Event, Effect, State>

    override fun onDestroyView() {
        super.onDestroyView()
        clearDisposables()
    }
}
