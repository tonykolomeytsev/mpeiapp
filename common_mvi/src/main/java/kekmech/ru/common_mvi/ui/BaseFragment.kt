package kekmech.ru.common_mvi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import kekmech.ru.common_mvi.util.DisposableDelegate
import kekmech.ru.common_mvi.util.DisposableDelegateImpl
import timber.log.Timber
import kekmech.ru.common_mvi.Feature as MviFeature

abstract class BaseFragment<Event : Any, Effect : Any, State : Any, Feature : MviFeature<State, Event, Effect>> : Fragment(),
    DisposableDelegate by DisposableDelegateImpl() {

    private val effects by lazy { feature.effects }

    protected lateinit var feature: Feature
    protected abstract val initEvent: Event
    @LayoutRes
    open var layoutId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        feature = createFeature().apply { accept(initEvent) }
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

    protected abstract fun createFeature(): Feature

    protected open fun render(state: State) = Unit

    protected open fun handleEffect(effect: Effect) = Unit

    protected fun Toolbar.init(@StringRes titleRes: Int? = null) =
        init(titleRes?.let { getString(it) })

    protected fun Toolbar.init(title: String?) {
        title?.let { setTitle(it) }
        setNavigationOnClickListener { requireActivity().onBackPressed() }
    }

    private fun observeState() {
        feature.states
            .skip(1)
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