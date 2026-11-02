package com.kekmech.lib_fragment

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation3.runtime.NavEntryDecorator
import java.io.Serializable
import kotlin.reflect.KClass

/**
 * Предоставляет каждому экрану `NavDisplay` свой [LocalResultController].
 *
 * Результаты ходят через `FragmentManager` активити: туда же их отправляют диалоги
 * ([ComposeBottomSheetDialogFragment]) и фрагменты-табы внутри `AndroidFragment`,
 * поэтому все экраны приложения обмениваются результатами через одну шину.
 */
@Composable
public fun <T : Any> rememberResultControllerNavEntryDecorator(
    activity: FragmentActivity,
): NavEntryDecorator<T> = remember(activity) {
    NavEntryDecorator { entry ->
        // NavDisplay выдает каждому экрану свой LifecycleOwner
        val lifecycleOwner = LocalLifecycleOwner.current
        val resultController = remember(lifecycleOwner) {
            NavEntryResultController(activity.supportFragmentManager, lifecycleOwner)
        }
        CompositionLocalProvider(LocalResultController provides resultController) {
            entry.Content()
        }
    }
}

internal class NavEntryResultController(
    private val fragmentManager: FragmentManager,
    private val lifecycleOwner: LifecycleOwner,
) : ResultController {

    override fun <T : Serializable> setResultListener(
        requestKey: String,
        resultClass: KClass<T>,
        listener: (T) -> Unit,
    ) {
        fragmentManager.setFragmentResultListener(requestKey, lifecycleOwner) { _, bundle ->
            val result = BundleCompat.getSerializable(bundle, requestKey, resultClass.javaObjectType)
                ?: return@setFragmentResultListener
            // Даем закончиться текущему изменению back stack'а, если внутри listener'а
            // сразу хотим куда-то навигироваться
            mainHandler.post {
                if (lifecycleOwner.lifecycle.currentState != Lifecycle.State.DESTROYED) {
                    listener(result)
                }
            }
        }
    }

    override fun clearResultListener(requestKey: String) {
        fragmentManager.clearFragmentResultListener(requestKey)
    }

    override fun <T : Serializable> setResult(requestKey: String, result: T) {
        fragmentManager.setFragmentResult(requestKey, bundleOf(requestKey to result))
    }

    private companion object {
        val mainHandler = Handler(Looper.getMainLooper())
    }
}
