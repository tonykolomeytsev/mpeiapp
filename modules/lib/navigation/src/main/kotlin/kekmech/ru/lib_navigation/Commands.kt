package kekmech.ru.lib_navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlin.reflect.KClass

public interface Command {
    public fun apply(backStack: MutableList<ScreenKey>)
}

public interface ActivityCommand {
    public fun apply(activity: Activity)
}

public class AddScreenForward(private val key: ScreenKey) : Command {

    override fun apply(backStack: MutableList<ScreenKey>) {
        backStack.add(key)
    }
}

public class ReplaceScreen(private val key: ScreenKey) : Command {

    override fun apply(backStack: MutableList<ScreenKey>) {
        backStack.removeLastOrNull()
        backStack.add(key)
    }
}

public class NewRoot(private vararg val keys: ScreenKey) : Command {

    override fun apply(backStack: MutableList<ScreenKey>) {
        require(keys.isNotEmpty()) { "NewRoot requires at least one screen" }
        backStack.clear()
        backStack.addAll(keys)
    }
}

/**
 * Закрывает текущий экран. Последний экран в back stack'е не закрывается,
 * выход из приложения обрабатывает система.
 */
public class PopBackStack : Command {

    override fun apply(backStack: MutableList<ScreenKey>) {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }
}

public class PopUntil(
    private val keyClass: KClass<out ScreenKey>,
    private val inclusive: Boolean = false,
) : Command {

    override fun apply(backStack: MutableList<ScreenKey>) {
        val index = backStack.indexOfLast { keyClass.isInstance(it) }
        if (index == -1) return
        val keepCount = if (inclusive) index else index + 1
        while (backStack.size > keepCount.coerceAtLeast(1)) {
            backStack.removeAt(backStack.lastIndex)
        }
    }
}

public class StartActivity(private val intentProvider: (Context) -> Intent) : ActivityCommand {

    override fun apply(activity: Activity) {
        activity.startActivity(intentProvider(activity))
    }
}
