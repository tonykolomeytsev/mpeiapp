package kekmech.ru.common_android

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.hasPermission(permissionName: String): Boolean =
    ContextCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED