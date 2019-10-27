package kekmech.ru.core

import android.util.Log

class FreqLimiter(val minTime: Long = 300L) {
    private var lastTimeMillis: Long = 0L
    operator fun invoke(): Boolean {
        if (System.currentTimeMillis() - lastTimeMillis > minTime) {
            lastTimeMillis = System.currentTimeMillis()
            return true
        } else {
            Log.d("FreqLmiter", "Action cancelled")
            return false
        }
    }
}