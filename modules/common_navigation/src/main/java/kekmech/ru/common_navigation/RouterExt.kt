package kekmech.ru.common_navigation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kekmech.ru.common_navigation.di.RouterHolder

fun Fragment.getRouter(): Router {
    return (requireActivity().applicationContext as RouterHolder).router
}

fun Fragment.addScreenForward(fragmentProvider: () -> Fragment) =
    getRouter().executeCommand(AddScreenForward(fragmentProvider))

fun Fragment.replaceScreen(fragmentProvider: () -> Fragment) =
    getRouter().executeCommand(ReplaceScreen(fragmentProvider))

fun Fragment.showDialog(fragmentProvider: () -> DialogFragment) =
    getRouter().executeCommand(ShowDialog(fragmentProvider))

fun Fragment.newRoot(fragmentProvider: () -> Fragment) =
    getRouter().executeCommand(NewRoot(fragmentProvider))

inline fun <reified T : Fragment> Fragment.popUntil(inclusive: Boolean = false) =
    getRouter().executeCommand(PopUntil(T::class, inclusive))
