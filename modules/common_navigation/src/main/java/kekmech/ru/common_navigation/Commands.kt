package kekmech.ru.common_navigation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import kotlin.reflect.KClass

interface Command {
    fun apply(supportFragmentManager: FragmentManager)
}

interface ActivityCommand {
    fun apply(activity: AppCompatActivity)
}

class AddScreenForward(private val fragmentProvider: () -> Fragment) : Command {

    override fun apply(supportFragmentManager: FragmentManager) = supportFragmentManager.commit {
        setDefaultAnimation()
        replace(R.id.container, fragmentProvider())
        addToBackStack(null)
    }
}

class ReplaceScreen(private val fragmentProvider: () -> Fragment) : Command {

    override fun apply(supportFragmentManager: FragmentManager) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.commit {
            setDefaultAnimation()
            replace(R.id.container, fragmentProvider())
            addToBackStack(null)
        }

    }
}

class ShowDialog(private val fragmentProvider: () -> DialogFragment) : Command {

    override fun apply(supportFragmentManager: FragmentManager) {
        fragmentProvider().show(supportFragmentManager, null)
    }
}

class NewRoot(private  val fragmentProvider: () -> Fragment) : Command {

    override fun apply(supportFragmentManager: FragmentManager) {
        ClearBackStack().apply(supportFragmentManager)
        supportFragmentManager.commit {
            setDefaultAnimation()
            replace(R.id.container, fragmentProvider())
            disallowAddToBackStack()
        }
    }
}

class ClearBackStack : Command {

    override fun apply(supportFragmentManager: FragmentManager) {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

class PopUntil<T : Fragment>(
    private val fragmentClass: KClass<T>,
    private val inclusive: Boolean = false
) : Command {

    override fun apply(supportFragmentManager: FragmentManager) {
        while (
            supportFragmentManager.fragments.lastOrNull()?.javaClass !in
            listOf<Class<out Fragment>?>(fragmentClass.java, null) // fixme
        ) {
            val result = supportFragmentManager.popBackStackImmediate()
            if (!result) break
        }
        if (
            inclusive &&
            supportFragmentManager.fragments.lastOrNull()?.javaClass == fragmentClass.java
        ) {
            supportFragmentManager.popBackStackImmediate()
        }
    }
}

class StartActivity(private val intentProvider: (Context) -> Intent) : ActivityCommand {

    override fun apply(activity: AppCompatActivity) {
        activity.startActivity(intentProvider(activity))
    }
}

private fun FragmentTransaction.setDefaultAnimation(): FragmentTransaction =
    setCustomAnimations(R.anim.anim_in, R.anim.anim_out, R.anim.anim_in, R.anim.anim_out)
