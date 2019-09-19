package kekmech.ru.mainscreen

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kekmech.ru.addscreen.AddFragment
import kekmech.ru.feed.FeedFragment
import kotlin.reflect.KClass

class MainNavController(private var activity: AppCompatActivity?) : LifecycleObserver {

    var onAddGroupListener: (Boolean) -> Unit = {}

    init {
        activity?.apply {
            lifecycle.addObserver(this@MainNavController)
        }
    }

    fun navigate(
        fragmentKClass: KClass<out Fragment>,
        addToBackStack: Boolean,
        enterAnim: Int = -1,
        exitAnim: Int = -1,
        transitionStyle: Int = -1,
        action: String = ""
    ) {
        val instance = fragment(fragmentKClass)
        instance.retainInstance = true
        if (instance is FeedFragment && action.isNotEmpty()) instance.notifyActionRequired(action)
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.apply {
                if (enterAnim != -1 && exitAnim != -1) setCustomAnimations(
                    enterAnim,
                    exitAnim
                )
            }
            ?.replace(R.id.nav_host_fragment, instance)
            ?.apply { if (addToBackStack) addToBackStack(fragmentKClass.java.simpleName) }
            ?.apply { if (transitionStyle != -1) setTransitionStyle(transitionStyle) }?.commit()
        if (instance is AddFragment) onAddGroupListener(true)
        else onAddGroupListener(false)
    }

    fun navigateWithTransition(fragmentKClass: KClass<out Fragment>, addToBackStack: Boolean) {
        val instance = fragment(fragmentKClass)
        instance.retainInstance = true
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.nav_host_fragment, instance)
            ?.apply { if (addToBackStack) addToBackStack(fragmentKClass.java.simpleName) }
            ?.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.commit()
    }

    fun popBackStack() {
        onAddGroupListener(false)
        activity?.supportFragmentManager?.apply {
            if (backStackEntryCount == 0)
                activity?.finish()
            else
                this.popBackStack()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onActivityPaused() {
        println("ACTIVITY PAUSED")
        activity = null
    }

    private fun <T : Fragment> fragment(fragmentKClass: KClass<T>): Fragment {
        //lazy init
        if (!fragmentsMap.containsKey(fragmentKClass)) {
            fragmentsMap[fragmentKClass] = fragmentKClass.java.newInstance()
        }
        return fragmentsMap[fragmentKClass]!!
    }

    fun onResume(activity: AppCompatActivity) {
        this.activity = activity
    }

    companion object {
        val fragmentsMap = mutableMapOf<KClass<*>, Fragment>()
    }
}