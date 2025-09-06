package kekmech.ru.lib_navigation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kekmech.ru.lib_navigation.di.RouterHolder

public fun Fragment.getRouter(): Router {
    return (requireActivity().applicationContext as RouterHolder).router
}

public fun Fragment.addScreenForward(fragmentProvider: () -> Fragment) {
    getRouter().executeCommand(AddScreenForward(fragmentProvider))
}

public fun Fragment.replaceScreen(fragmentProvider: () -> Fragment) {
    getRouter().executeCommand(ReplaceScreen(fragmentProvider))
}

public fun Fragment.showDialog(fragmentProvider: () -> DialogFragment) {
    getRouter().executeCommand(ShowDialog(fragmentProvider))
}

public fun Fragment.newRoot(fragmentProvider: () -> Fragment) {
    getRouter().executeCommand(NewRoot(fragmentProvider))
}

public inline fun <reified T : Fragment> Fragment.popUntil(inclusive: Boolean = false) {
    getRouter().executeCommand(PopUntil(T::class, inclusive))
}
