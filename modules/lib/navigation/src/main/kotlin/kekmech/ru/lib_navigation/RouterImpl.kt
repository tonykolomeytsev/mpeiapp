package kekmech.ru.lib_navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

internal class RouterImpl : NavigationHolder, Router {

    private var activity: AppCompatActivity? = null

    private val activityCommandBuffer = mutableListOf<ActivityCommand>()
    private val commandBuffer = mutableListOf<Command>()

    override fun subscribe(activity: AppCompatActivity) {
        this.activity = activity
        if (commandBuffer.isNotEmpty()) {
            val pendingCommands = ArrayList(commandBuffer)
            commandBuffer.clear()
            pendingCommands.forEach { it.apply(activity.supportFragmentManager) }
        }
        if (activityCommandBuffer.isNotEmpty()) {
            val pendingActivityCommands = ArrayList(activityCommandBuffer)
            activityCommandBuffer.clear()
            pendingActivityCommands.forEach { it.apply(activity) }
        }
    }

    override fun unsubscribe() {
        activity = null
    }

    override fun executeCommand(vararg commands: Command) {
        commands.forEach { command ->
            activity?.supportFragmentManager?.let {
                executeCommand(it, command)
            } ?: run {
                commandBuffer.add(command)
            }
        }
    }

    override fun executeCommand(command: ActivityCommand) {
        activity?.let {
            command.apply(it)
        } ?: run {
            activityCommandBuffer.add(command)
        }
    }

    override fun executeCommand(fragmentManager: FragmentManager, command: Command) {
        command.apply(fragmentManager)
    }
}
