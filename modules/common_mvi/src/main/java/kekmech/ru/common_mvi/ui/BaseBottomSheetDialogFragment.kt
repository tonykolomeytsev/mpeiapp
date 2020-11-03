package kekmech.ru.common_mvi.ui

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import kekmech.ru.common_mvi.util.DisposableDelegate
import kekmech.ru.common_mvi.util.DisposableDelegateImpl
import timber.log.Timber
import kekmech.ru.common_mvi.Feature as MviFeature

abstract class BaseBottomSheetDialogFragment<Event : Any, Effect : Any, State : Any, Feature : MviFeature<State, Event, Effect>> :
    BottomSheetDialogFragment(),
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            dialogInterface as BottomSheetDialog
            dialogInterface.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.setBackgroundColor(Color.TRANSPARENT)
        }
        return dialog
    }

    protected open fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) = Unit

    protected abstract fun createFeature(): Feature

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