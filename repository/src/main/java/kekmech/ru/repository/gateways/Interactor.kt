package kekmech.ru.repository.gateways

import kotlinx.coroutines.delay
import kotlin.Exception

abstract class Interactor<T : Any> {
    private var onFailure: (e: Exception) -> Unit = {}
    private var onSuccess: () -> Unit = {}
    private var attempts = 1
    private var delayTime: Long = 0L

    suspend fun invoke(): T? {
        var value: T? = null
        var success = false
        var ex: Exception? = null
        while (attempts > 0 && !success) {
            try {
                value = onStart()
                success = true
            } catch (e: Exception) { e.printStackTrace(); ex = e }
            finally {
                attempts--
                delay(delayTime)
            }
        }

        if (success) onSuccess()
        else onFailure(ex!!)
        return value
    }

    abstract suspend fun onStart(): T

    fun setAttempts(attemptsCount: Int): Interactor<T> {
        this.attempts = attemptsCount
        return this
    }

    fun setDelay(delayTimeMillis: Long): Interactor<T> {
        this.delayTime = delayTimeMillis
        return this
    }

    fun setOnSuccessListener(listener: () -> Unit): Interactor<T> {
        this.onSuccess = listener
        return this
    }

    fun setOnFailureListener(listener: (e: Exception) -> Unit): Interactor<T> {
        this.onFailure = listener
        return this
    }
}