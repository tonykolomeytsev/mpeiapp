package kekmech.ru.lib_navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateListOf
import androidx.core.os.BundleCompat

internal class RouterImpl : NavigationHolder, Router {

    private var activity: AppCompatActivity? = null

    private val activityCommandBuffer = mutableListOf<ActivityCommand>()
    override val backStack: List<ScreenKey>
        field: MutableList<ScreenKey> = mutableStateListOf<ScreenKey>()

    override fun subscribe(activity: AppCompatActivity) {
        this.activity = activity
        val bufferedCommands = activityCommandBuffer.toList()
        activityCommandBuffer.clear()
        bufferedCommands.forEach { it.apply(activity) }
    }

    override fun unsubscribe() {
        activity = null
    }

    override fun executeCommand(vararg commands: Command) {
        commands.forEach { it.apply(backStack) }
    }

    override fun executeCommand(command: ActivityCommand) {
        activity?.let(command::apply) ?: activityCommandBuffer.add(command)
    }

    override fun saveState(): Bundle = Bundle().apply {
        putParcelableArrayList(KEY_BACK_STACK, ArrayList(backStack))
    }

    override fun restoreState(state: Bundle) {
        // Роутер - синглтон, поэтому после пересоздания активити back stack уже на месте.
        // Восстанавливаем только после смерти процесса
        if (backStack.isNotEmpty()) return
        val restored =
            BundleCompat.getParcelableArrayList(state, KEY_BACK_STACK, ScreenKey::class.java)
        if (!restored.isNullOrEmpty()) backStack.addAll(restored)
    }

    private companion object {
        const val KEY_BACK_STACK = "Router.BackStack"
    }
}
