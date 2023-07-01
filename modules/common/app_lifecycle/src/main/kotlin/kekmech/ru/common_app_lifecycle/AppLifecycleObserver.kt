package kekmech.ru.common_app_lifecycle

import android.content.Context

/**
 * An interface whose implementations can be provided via multibinding
 * to be notified of the MpeixApp events.
 *
 * If you need to observe MainActivity events, use [MainActivityLifecycleObserver].
 *
 * Creating your implementation:
 * ```kotlin
 * internal class MyInitializer(...) : AppLifecycleObserver {
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
 *     single(MultibindingQualifier) { MyInitializer(...) } bind AppLifecycleObserver::class
 *     singleOf(::MyInitializer, MultibindingQualifier) bind AppLifecycleObserver::class
 *     // or factory
 *     factory(MultibindingQualifier) { MyInitializer(...) } bind AppLifecycleObserver::class
 *     factory(::MyInitializer, MultibindingQualifier) bind AppLifecycleObserver::class
 * }
 * ```
 *
 * @see MainActivityLifecycleObserver
 * @see onCreate
*/
interface AppLifecycleObserver {

    /**
     * This method be called at the same time as MpeixApp.onCreate()
     */
    fun onCreate(context: Context) = Unit
}
