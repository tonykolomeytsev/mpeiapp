package kekmech.ru.common_mvi

import kekmech.ru.common_android.fragment.BottomSheetDialogFragment
import vivid.money.elmslie.android.screen.ElmDelegate
import vivid.money.elmslie.android.screen.ElmScreen

abstract class BaseBottomSheetDialogFragment<Event : Any, Effect : Any, State : Any> :
    BottomSheetDialogFragment(),
    ElmDelegate<Event, Effect, State>,
    DisposableDelegate by DisposableDelegateImpl() {

    protected val feature by lazy(LazyThreadSafetyMode.NONE) { screen.store }

    @Suppress("LeakingThis")
    private val screen = ElmScreen(this, lifecycle) { requireActivity() }

    override fun onDestroyView() {
        super.onDestroyView()
        clearDisposables()
    }
}