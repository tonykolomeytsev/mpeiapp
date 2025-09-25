package kekmech.ru.ext_android

import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import java.io.Serializable

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
    noinline resultMapper: (Bundle, key: String) -> T,
    crossinline onResult: (T) -> Unit,
) {
    setFragmentResultListener(key) { requestKey, bundle ->
        onResult(resultMapper(bundle, requestKey))
    }
}

public fun Fragment.setResult(key: String, result: Any = EmptyResult) {
    setFragmentResult(key, bundleOf(key to result))
}

// region: ARGUMENTS

public fun Fragment.notNullStringArg(key: String): String = requireNotNull(stringArg(key))
public fun Fragment.stringArg(key: String): String? = arguments?.getString(key, null)

public fun Fragment.booleanArg(key: String): Boolean = arguments?.getBoolean(key, false) ?: false

public inline fun <reified T : Serializable> Fragment.notNullSerializableArg(key: String): T =
    requireNotNull(serializableArg(key))

public inline fun <reified T : Serializable> Fragment.serializableArg(key: String): T? =
    arguments?.let { BundleCompat.getSerializable(it, key, T::class.java) }

public fun Fragment.removeArg(key: String) {
    arguments?.remove(key)
}

// endregion: ARGUMENTS

public fun Fragment.hideKeyboard() {
    view?.let { KeyboardUtils.hideSoftInput(it) }
}

public fun Fragment.onKeyboardShown(action: () -> Unit) {
    view?.doOnApplyWindowInsets { _, windowInsets, _ ->
        val imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime())
        if (imeInsets.bottom > AVERAGE_KEYBOARD_HEIGHT) action()
    }
}
