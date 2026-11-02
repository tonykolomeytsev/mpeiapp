package kekmech.ru.lib_navigation

public interface Router {

    /**
     * Текущий back stack. Список observable для Compose, последний элемент - видимый экран.
     */
    public val backStack: List<ScreenKey>

    public fun executeCommand(vararg commands: Command)

    public fun executeCommand(command: ActivityCommand)
}
