package kekmech.ru.core.platform

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import dagger.android.support.DaggerFragment
import kekmech.ru.core.Presenter

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<P : Presenter<L>, L : LifecycleOwner>(
    private val layoutId: Int,
    private val statusBarColor: Int = Color.TRANSPARENT
) : DaggerFragment() {
    abstract var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate(this as L)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BaseKeyboardObserver(view as ViewGroup, this::onKeyboardVisibilityChanged)
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.statusBarColor = statusBarColor
        presenter.onResume(this as L)
    }

    override fun onPause() {
        presenter.onPause(this as L)
        super.onPause()
    }

    open fun onKeyboardVisibilityChanged(visible: Boolean) = Unit
}