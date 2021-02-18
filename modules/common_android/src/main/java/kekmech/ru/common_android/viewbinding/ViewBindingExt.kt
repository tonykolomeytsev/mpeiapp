package kekmech.ru.common_android.viewbinding

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kekmech.ru.common_android.R
import kotlin.properties.ReadOnlyProperty

fun <F : Fragment, VB : ViewBinding> Fragment.viewBinding(
    viewBinder: (F) -> VB
): ReadOnlyProperty<F, VB> {
    return object : ViewBindingProperty<F, VB>(viewBinder) {
        override fun getLifecycleOwner(thisRef: F) = viewLifecycleOwner
    }
}

inline fun <F : Fragment, T : ViewBinding> Fragment.viewBinding(
    crossinline vbFactory: (View) -> T,
    crossinline viewProvider: (F) -> View = Fragment::requireView
): ReadOnlyProperty<F, T> {
    return viewBinding { fragment: F -> vbFactory(viewProvider(fragment)) }
}

fun <F : BottomSheetDialogFragment, VB : ViewBinding> BottomSheetDialogFragment.viewBinding(
    viewBinder: (F) -> VB
): ReadOnlyProperty<F, VB> {
    return object : ViewBindingProperty<F, VB>(viewBinder) {
        override fun getLifecycleOwner(thisRef: F) = viewLifecycleOwner
    }
}

inline fun <F : BottomSheetDialogFragment, VB : ViewBinding> BottomSheetDialogFragment.viewBinding(
    crossinline vbFactory: (View) -> VB,
    crossinline viewProvider: (F) -> View = Fragment::requireView
): ReadOnlyProperty<F, VB> {
    return viewBinding { fragment: F -> vbFactory(viewProvider(fragment)) }
}

fun <F : DialogFragment, VB : ViewBinding> DialogFragment.viewBinding(
    viewBinder: (F) -> VB
): ReadOnlyProperty<F, VB> {
    return object : ViewBindingProperty<F, VB>(viewBinder) {
        override fun getLifecycleOwner(thisRef: F) = // вот этого момента я пока не понимаю
            if (thisRef.view == null) thisRef.viewLifecycleOwner else thisRef
    }
}

inline fun <F : DialogFragment, VB : ViewBinding> DialogFragment.viewBinding(
    crossinline vbFactory: (View) -> VB,
    crossinline viewProvider: (F) -> View = Fragment::requireView
): ReadOnlyProperty<F, VB> {
    return viewBinding { fragment: F -> vbFactory(viewProvider(fragment)) }
}

inline fun <T : ViewBinding> T.unit(block: T.() -> Unit) { apply(block) }

fun <T : View> ReusableViewHolder.lazyBinding(@IdRes idRes: Int) =
    lazy(LazyThreadSafetyMode.NONE) { containerView.findViewById<T>(idRes) }