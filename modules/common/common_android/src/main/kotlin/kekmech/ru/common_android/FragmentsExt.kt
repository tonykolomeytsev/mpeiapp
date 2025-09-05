package kekmech.ru.common_android

import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener

private const val AVERAGE_KEYBOARD_HEIGHT = 250

public fun Fragment.close() {
    when {
        this is DialogFragment -> dismiss()
        parentFragmentManager.backStackEntryCount > 0 -> parentFragmentManager.popBackStack()
        parentFragment != null -> parentFragment?.close()
        else -> activity?.finish()
    }
}

public fun <T : Fragment> T.withArguments(vararg args: Pair<String, Any?>): T = apply {
    arguments?.putAll(bundleOf(*args)) ?: run { arguments = bundleOf(*args) }
}

public inline fun <reified T : Any> Fragment.setResultListener(
    key: String,
    crossinline onResult: (T) -> Unit,
) {
    setFragmentResultListener(key) { requestKey, bundle ->
        onResult(bundle.getArgument(requestKey))
    }
}

public fun Fragment.setResult(key: String, result: Any = EmptyResult) {
    setFragmentResult(key, bundleOf(key to result))
}

public inline fun <reified T : Any> Fragment.getArgument(key: String): T = arguments.getArgument(key)

public inline fun <reified T : Any> Fragment.findArgument(key: String): T? = arguments.findArgument(key)

public inline fun <reified T : Any> Fragment.findAndRemoveArgument(key: String): T? =
    findArgument<T>(key).also { arguments?.remove(key) }

public fun Fragment.hideKeyboard() {
    view?.let { KeyboardUtils.hideSoftInput(it) }
}

public fun Fragment.onKeyboardShown(action: () -> Unit) {
    view?.doOnApplyWindowInsets { _, windowInsets, _ ->
        if (windowInsets.systemWindowInsetBottom > AVERAGE_KEYBOARD_HEIGHT) action()
    }
}

public fun Fragment.setOnBackPressedListener(listener: () -> Unit) {
    activity?.onBackPressedDispatcher?.addCallback(
        viewLifecycleOwner,
        object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                listener.invoke()
            }
        }
    )
}
