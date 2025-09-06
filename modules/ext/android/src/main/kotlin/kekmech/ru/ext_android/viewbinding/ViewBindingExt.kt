package kekmech.ru.ext_android.viewbinding

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.jetbrains.annotations.ApiStatus
import kotlin.properties.ReadOnlyProperty

@ApiStatus.Internal
public fun <F : Fragment, VB : ViewBinding> Fragment.viewBinding(
    viewBinder: (F) -> VB,
): ReadOnlyProperty<F, VB> {
    return ViewBindingProperty(viewBinder)
}

public inline fun <F : Fragment, T : ViewBinding> Fragment.viewBinding(
    crossinline vbFactory: (View) -> T,
    crossinline viewProvider: (F) -> View = Fragment::requireView,
): ReadOnlyProperty<F, T> {
    return viewBinding { fragment: F -> vbFactory(viewProvider(fragment)) }
}

public inline fun <F : BottomSheetDialogFragment, VB : ViewBinding> BottomSheetDialogFragment.viewBinding(
    crossinline vbFactory: (View) -> VB,
    crossinline viewProvider: (F) -> View = Fragment::requireView,
): ReadOnlyProperty<F, VB> {
    return viewBinding { fragment: F -> vbFactory(viewProvider(fragment)) }
}

public fun <T : View> ReusableViewHolder.lazyBinding(@IdRes idRes: Int): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { containerView.findViewById<T>(idRes) }
