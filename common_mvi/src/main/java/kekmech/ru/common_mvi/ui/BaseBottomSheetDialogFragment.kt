package kekmech.ru.common_mvi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import timber.log.Timber
import kekmech.ru.common_mvi.util.DisposableDelegate
import kekmech.ru.common_mvi.util.DisposableDelegateImpl
import kekmech.ru.common_mvi.Feature as MviStore

abstract class BaseBottomSheetDialogFragment<Event, Effect, State, Store : MviStore<Event, Effect, State>> :
    BottomSheetDialogFragment(),
    DisposableDelegate by DisposableDelegateImpl() {

    private val effects by lazy { store.effects }

    protected lateinit var store: Store
    protected abstract val initEvent: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = createStore().apply { accept(initEvent) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(getLayoutRes(), container, false)

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeEffects()

        onViewCreatedInternal(view, savedInstanceState)

        render(store.states.blockingFirst())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearDisposables()
    }

    override fun onDestroy() {
        super.onDestroy()
        store.dispose()
    }

    fun getCurrentState(): State {
        return store.states.blockingFirst()
    }

    protected open fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        // empty
    }

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    protected abstract fun createStore(): Store

    protected abstract fun render(state: State)

    protected open fun handleEffect(effect: Effect) {
        // no handling by default
    }

    private fun observeState() {
        store.states
            .skip(1) // skipped first state, because we need to avoid rendering initial state twice
            .observeOn(mainThread())
            .subscribe({
                render(it)
            }, {
                Timber.e(it)
            })
            .bind()
    }

    private fun observeEffects() {
        effects
            .observeOn(mainThread())
            .subscribe({
                handleEffect(it)
            }, {
                Timber.e(it)
            })
            .bind()
    }
}