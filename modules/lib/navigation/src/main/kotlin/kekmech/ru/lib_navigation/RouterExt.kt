package kekmech.ru.lib_navigation

import androidx.fragment.app.Fragment
import kekmech.ru.lib_navigation.di.RouterHolder

public fun Fragment.getRouter(): Router {
    return (requireActivity().applicationContext as RouterHolder).router
}

public fun Fragment.addScreenForward(key: ScreenKey) {
    getRouter().executeCommand(AddScreenForward(key))
}
