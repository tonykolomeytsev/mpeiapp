package kekmech.ru.ext_android

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat
import java.io.Serializable

public fun Bundle.string(key: String): String? = getString(key)

public fun Bundle.notNullString(key: String): String = requireNotNull(getString(key))

public fun Bundle.bool(key: String): Boolean = getBoolean(key, false)

public inline fun <reified T : Serializable> Bundle.serializable(key: String): T? =
    BundleCompat.getSerializable(this, key, T::class.java)

public inline fun <reified T : Serializable> Bundle.notNullSerializable(key: String): T =
    requireNotNull(serializable(key))

public inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? =
    BundleCompat.getParcelable(this, key, T::class.java)