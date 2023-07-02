package kekmech.ru.library_app_lifecycle

import android.content.Context

/**
 * An interface whose implementations can be provided via multibinding
 * to be notified of the MainActivity events.
 *
 * If you need to observe MpeixApp events, use [AppLifecycleObserver].
 *
 * Creating your implementation:
 * ```kotlin
 * internal class MyInitializer(...) : MainActivityLifecycleObserver {
 *     override fun onCreate(context: Context) {
 *         // initialize your stuff
 *     }
 * }
 * ```
 *
 * Registering your own implementation:
 * ```kotlin
 * val MyFeatureModule = module {
 *     val MultibindingQualifier = named("FeatureNotesModule")
 *
 *     // either singleton
 *     single(MultibindingQualifier) { MyInitializer(...) } bind MainActivityLifecycleObserver::class
 *     singleOf(::MyInitializer, MultibindingQualifier) bind MainActivityLifecycleObserver::class
 *     // or factory
 *     factory(MultibindingQualifier) { MyInitializer(...) } bind MainActivityLifecycleObserver::class
 *     factory(::MyInitializer, MultibindingQualifier) bind MainActivityLifecycleObserver::class
 * }
 * ```
 *
 * @see AppLifecycleObserver
 * @see onCreate
 */
interface MainActivityLifecycleObserver {

    /**
     * This method be called at the same time as MainActivity.onCreate()
     */
    fun onCreate(context: Context) = Unit
}
