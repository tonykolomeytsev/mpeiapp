package kekmech.ru.common_navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class RouterImpl : NavigationHolder, Router {

    private var activity: AppCompatActivity? = null

    private var activityCommandBuffer = mutableListOf<ActivityCommand>()
    private val commandBuffer = mutableListOf<Command>()

    override fun subscribe(activity: AppCompatActivity) {
        this.activity = activity
        commandBuffer
            .map { it }
            .forEach {
                it.apply(activity.supportFragmentManager)
                commandBuffer.remove(it)
            }
        activityCommandBuffer
            .map { it }
            .forEach {
                it.apply(activity)
                activityCommandBuffer.remove(it)
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