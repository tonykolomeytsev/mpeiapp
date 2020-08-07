package kekmech.ru.common_android

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

private const val AVERAGE_KEYBOARD_HEIGHT = 250

const val RESULT_ERROR = 1
const val EXTRA_ERROR = "error"

fun Fragment.close() {
    when {
        this is DialogFragment -> dismiss()
        parentFragmentManager.backStackEntryCount > 0 -> parentFragmentManager.popBackStack()
        parentFragment != null -> parentFragment?.close()
        else -> activity?.finish()
    }
}

fun <T : Fragment> T.closeWithSuccess() = closeWithResult { this }

fun <T : Fragment> T.closeWithResult(putResult: Intent.() -> Intent) {
    close()
    targetFragment?.onActivityResult(targetRequestCode, RESULT_OK, Intent().putResult())
}

fun <T : Fragment> T.closeWithError(error: Throwable) {
    close()
    targetFragment?.onActivityResult(targetRequestCode, RESULT_ERROR, Intent().putExtra(EXTRA_ERROR, error))
}

fun <T : Fragment> T.withArguments(vararg args: Pair<String, Any?>): T = apply {
    arguments?.putAll(bundleOf(*args)) ?: run { arguments = bundleOf(*args) }
}

inline fun <reified T : Any> Fragment.getArgument(key: String): T = arguments.getargument(key)

inline fun <reified T : Any> Fragment.findArgument(key: String): T? = arguments.findArgument(key)

inline fun <reified T : Any> Fragment.findAndRemoveArgument(key: String): T? =
    findArgument<T>(key).also { arguments?.remove(key) }

fun <T : Fragment> T.withResultFor(target: Fragment, requestCode: Int): T {
    return also { it.setTargetFragment(target, requestCode) }
}

fun Fragment.hideKeyboard() {
    view?.let { KeyboardUtils.hideSoftInput(it) }
}

fun Fragment.onKeyboardShown(action: () -> Unit) {
    view?.doOnApplyWindowInsets { _, windowInsets, _ ->
        if (windowInsets.systemWindowInsetBottom > AVERAGE_KEYBOARD_HEIGHT) action()
    }
}

val Fragment.decorView: View
    get() = requireActivity().window.decorView.findViewById(android.R.id.content)