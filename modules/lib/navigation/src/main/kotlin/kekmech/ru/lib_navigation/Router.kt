package kekmech.ru.lib_navigation

import androidx.fragment.app.FragmentManager

public interface Router {

    public fun executeCommand(vararg commands: Command)

    public fun executeCommand(command: ActivityCommand)

    public fun executeCommand(fragmentManager: FragmentManager, command: Command)

}
