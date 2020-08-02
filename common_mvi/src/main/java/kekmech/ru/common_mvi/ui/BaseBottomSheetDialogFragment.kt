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
import kekmech.ru.common_mvi.Feature as MviFeature

abstract class BaseBottomSheetDialogFragment<State : Any, Event : Any, Effect : Any, Feature : MviFeature<State, Event, Effect>> :
    BottomSheetDialogFragment(),
    DisposableDelegate by DisposableDelegateImpl() {

    private val effects by lazy { feature.effects }

    protected lateinit var feature: Feature
    protected abstract val initEvent: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        feature = createStore().apply { accept(initEvent) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(getLayoutRes(), container, false)

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeEffects()

        onViewCreatedInternal(view, savedInstanceState)

        render(feature.states.blockingFirst())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearDisposables()
    }

    override fun onDestroy() {
        super.onDestroy()
        feature.dispose()
    }

    protected open fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) = Unit

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    protected abstract fun createStore(): Feature

    protected abstract fun render(state: State)

    protected open fun handleEffect(effect: Effect) {
        // no handling by default
    }

    private fun observeState() {
        feature.states
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