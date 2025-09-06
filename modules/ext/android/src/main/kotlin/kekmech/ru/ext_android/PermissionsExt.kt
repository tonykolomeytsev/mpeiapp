package kekmech.ru.ext_android

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

public fun Context.hasPermission(permissionName: String): Boolean =
    ContextCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED
