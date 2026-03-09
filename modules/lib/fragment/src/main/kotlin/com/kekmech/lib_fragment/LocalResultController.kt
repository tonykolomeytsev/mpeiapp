package com.kekmech.lib_fragment

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import java.io.Serializable
import kotlin.reflect.KClass

/**
 * Обертка Fragment Result API, для использования внутри Compose экранов.
 *
 * @see setResult
 * @see setResultListener
 */
@Stable
public interface ResultController {

    /**
     * Вернуть результат экрану, который его запрашивал.
     * Чтобы вернуть пустой результат, используй [EmptyResult].
     *
     * ### Пример использования
     * ```
     * // state.resultKey == REQUEST_KEY
     * val resultController = LocalResultController.current
     * Button(
     *     text = "Return result",
     *     onClick = {
     *         resultController.setResult(state.resultKey, "Hello")
     *         router.exit()
     *     }
     * )
     * ```
     *
     * @see setResultListener
     */
    public fun <T : Serializable> setResult(requestKey: String, result: T)

    /**
     * Подписаться на получение результата другого экрана.
     *
     * ### Пример использования
     * ```kotlin
     * Button(
     *     text = "Request result",
     *     onClick = {
     *         resultController.setResultListener<String>(REQUEST_KEY) { result ->
     *             println("Returned result: $result")
     *         }
     *         router.navigateTo(SomeScreen(REQUEST_KEY))
     *     }
     * )
     * ```
     *
     * @see setResult
     */
    public fun <T : Serializable> setResultListener(
        requestKey: String,
        resultClass: KClass<T>,
        listener: (T) -> Unit,
    )
}

/**
 * Подписаться на получение результата другого экрана.
 *
 * ### Пример использования
 * ```kotlin
 * Button(
 *     text = "Request result",
 *     onClick = {
 *         resultController.setResultListener<String>(REQUEST_KEY) { result ->
 *             println("Returned result: $result")
 *         }
 *         router.navigateTo(SomeScreen(REQUEST_KEY))
 *     }
 * )
 * ```
 *
 * @see ResultController.setResult
 */
public inline fun <reified T : Serializable> ResultController.setResultListener(
    requestKey: String,
    noinline listener: (T) -> Unit,
): Unit = setResultListener(requestKey, T::class, listener)

/**
 * Литерал для пустого результата [ResultController].
 */
public object EmptyResult : Serializable {
    @Suppress("Unused")
    private fun readResolve(): Any = EmptyResult
}

/**
 * Ручка для доступа к локальному [ResultController] текущего экрана.
 *
 * Может быть использована для ожидания результата другого экрана,
 * или для возврата результата с текущего экрана.
 *
 * ### Подписка на результат на другом экране:
 * ```kotlin
 * Button(
 *     text = "Request result",
 *     onClick = {
 *         resultController.setResultListener<String>(REQUEST_KEY) { result ->
 *             println("Returned result: $result")
 *         }
 *         router.navigateTo(SomeScreen(REQUEST_KEY))
 *     }
 * )
 * ```
 *
 * ### Возврат результата с текущего экрана:
 * ```
 * // state.resultKey == REQUEST_KEY
 * val resultController = LocalResultController.current
 * Button(
 *     text = "Return result",
 *     onClick = {
 *         resultController.setResult(state.resultKey, "Hello")
 *         router.exit()
 *     }
 * )
 * ```
 */
public val LocalResultController: ProvidableCompositionLocal<ResultController> =
    staticCompositionLocalOf { error("No LocalResultController provided!") }

internal class FragmentResultController(
    private val fragment: Fragment,
) : ResultController {

    override fun <T : Serializable> setResultListener(
        requestKey: String,
        resultClass: KClass<T>,
        listener: (T) -> Unit,
    ) {
        // Фрагменты, находящиеся внутри MainFragment (экраны-табы) не могут поймать
        // Fragment Result отправленный любым другим фрагментом, поэтому для таких экранов
        // размещаем подписку в родительском FragmentManager (принадл. MainFragment)
        val targetFragment = fragment.parentFragment ?: fragment

        targetFragment.setFragmentResultListener(requestKey) { key, bundle ->
            if (key == requestKey) {
                @Suppress("UNCHECKED_CAST", "DEPRECATION")
                val result: T? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getSerializable(requestKey, resultClass.java)
                } else {
                    bundle.getSerializable(requestKey) as T?
                }
                // Для случаев, когда внутри result listener'а сразу хотим куда-то навигироваться.
                // Если запустить транзакцию fragment manager'а прямо здесь, то приложение упадет,
                // поэтому добавляем вызов колбэка в очередь, а не вызываем сразу
                Handler(Looper.getMainLooper()).post {
                    result?.let(listener)
                }
            }
        }
    }

    override fun <T : Serializable> setResult(requestKey: String, result: T) {
        fragment.setFragmentResult(requestKey, bundleOf(requestKey to result))
    }
}

