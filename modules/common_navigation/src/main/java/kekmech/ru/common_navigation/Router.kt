package kekmech.ru.common_navigation

import androidx.fragment.app.FragmentManager

interface Router {

    fun executeCommand(vararg commands: Command)

    fun executeCommand(command: ActivityCommand)

    fun executeCommand(fragmentManager: FragmentManager, command: Command)

}