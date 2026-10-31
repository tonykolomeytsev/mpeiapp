package com.kekmech.lib_fragment

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
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
     * Чтобы вернуть пустой результат, используй [kekmech.ru.ext_android.EmptyResult].
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
     * Подписка не переживает пересоздание фрагмента, поэтому в большинстве случаев
     * удобнее использовать [ResultListener].
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

    /**
     * Отписаться от получения результата по ключу [requestKey].
     */
    public fun clearResultListener(requestKey: String)
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
 * Подписаться на получение результата другого экрана на все время жизни текущего экрана.
 *
 * В отличие от [ResultController.setResultListener], вызванного из `onClick`, подписка
 * восстанавливается при каждом пересоздании композиции (возврат по back stack,
 * поворот экрана, восстановление после смерти процесса), поэтому результат,
 * отправленный пока экран не был виден, будет доставлен после его пересоздания.
 *
 * Вызывай на верхнем уровне экрана, а не внутри условных веток: подписка не снимается
 * при выходе [ResultListener] из композиции, она живет вместе с фрагментом.
 *
 * ### Пример использования
 * ```kotlin
 * ResultListener<String>(REQUEST_KEY) { result ->
 *     println("Returned result: $result")
 * }
 * Button(
 *     text = "Request result",
 *     onClick = { router.navigateTo(SomeScreen(REQUEST_KEY)) }
 * )
 * ```
 */
@Composable
public inline fun <reified T : Serializable> ResultListener(
    requestKey: String,
    noinline onResult: (T) -> Unit,
): Unit = ResultListener(requestKey, T::class, onResult)

/**
 * @see ResultListener
 */
@Composable
public fun <T : Serializable> ResultListener(
    requestKey: String,
    resultClass: KClass<T>,
    onResult: (T) -> Unit,
) {
    val resultController = LocalResultController.current
    val currentOnResult by rememberUpdatedState(onResult)
    DisposableEffect(resultController, requestKey, resultClass) {
        resultController.setResultListener(requestKey, resultClass) { currentOnResult(it) }
        // Не отписываемся: при переходе на другой экран view фрагмента уничтожается вместе
        // с композицией, а результат должен дойти до нас после возврата назад
        onDispose { }
    }
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

    // Фрагменты, находящиеся внутри MainFragment (экраны-табы) не могут поймать
    // Fragment Result отправленный любым другим фрагментом, поэтому для таких экранов
    // размещаем подписку в родительском FragmentManager (принадл. MainFragment).
    // Предполагается не более одного уровня вложенности.
    private val listenerFragmentManager: FragmentManager
        get() = fragment.parentFragment?.parentFragmentManager ?: fragment.parentFragmentManager

    override fun <T : Serializable> setResultListener(
        requestKey: String,
        resultClass: KClass<T>,
        listener: (T) -> Unit,
    ) {
        // Lifecycle owner'ом всегда остается текущий фрагмент, чтобы подписка
        // не пережила экран, который ее создал
        listenerFragmentManager.setFragmentResultListener(requestKey, fragment) { _, bundle ->
            val result = BundleCompat.getSerializable(bundle, requestKey, resultClass.javaObjectType)
                ?: return@setFragmentResultListener
            // Для случаев, когда внутри result listener'а сразу хотим куда-то навигироваться.
            // Если запустить транзакцию fragment manager'а прямо здесь, то приложение упадет,
            // поэтому добавляем вызов колбэка в очередь, а не вызываем сразу
            mainHandler.post {
                if (fragment.lifecycle.currentState != Lifecycle.State.DESTROYED) {
                    listener(result)
                }
            }
        }
    }

    override fun clearResultListener(requestKey: String) {
        listenerFragmentManager.clearFragmentResultListener(requestKey)
    }

    override fun <T : Serializable> setResult(requestKey: String, result: T) {
        fragment.setFragmentResult(requestKey, bundleOf(requestKey to result))
    }

    private companion object {
        val mainHandler = Handler(Looper.getMainLooper())
    }
}
