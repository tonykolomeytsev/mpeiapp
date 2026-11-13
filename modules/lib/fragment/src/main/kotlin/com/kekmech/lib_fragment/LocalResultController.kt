package com.kekmech.lib_fragment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
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
     * Подписка не переживает пересоздание экрана, поэтому в большинстве случаев
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
 * поворот экрана, восстановление после смерти процесса). Пока экран не в композиции,
 * подписки нет, и `FragmentManager` придерживает отправленный результат до тех пор,
 * пока экран не вернется и не подпишется снова.
 *
 * Вызывай на верхнем уровне экрана, а не внутри условных веток.
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
        onDispose { resultController.clearResultListener(requestKey) }
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
