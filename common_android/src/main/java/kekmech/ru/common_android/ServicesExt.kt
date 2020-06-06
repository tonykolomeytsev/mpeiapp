package kekmech.ru.common_android

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

@Suppress("DEPRECATION")
fun Context.vibrateSingle(durationMs: Long, amplitude: Int? = null) {
    if (!hasPermission(android.Manifest.permission.VIBRATE)) return
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val resultAmplitude = amplitude ?: VibrationEffect.DEFAULT_AMPLITUDE
        vibrator.vibrate(VibrationEffect.createOneShot(durationMs, resultAmplitude))
    } else {
        vibrator.vibrate(durationMs)
    }
}