package kekmech.ru.common_android

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.full.isSubclassOf

inline fun <reified T : Any> Bundle?.getargument(key: String): T =
    findArgument(key) ?: error("Argument with key = $key required")

@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified T : Any> Bundle?.findArgument(key: String): T? =
    findArgument(key) {
        val type = T::class
        when {
            type == String::class -> getString(key)
            type == Int::class -> getInt(key)
            type == Long::class -> getLong(key)
            type == Boolean::class -> getBoolean(key)
            type == Set::class -> getSerializable(key)
            type.isSubclassOf(CharSequence::class) -> getCharSequence(key)
            type.isSubclassOf(Parcelable::class) -> getParcelable(key)
            type.isSubclassOf(Serializable::class) -> getSerializable(key)
            else -> error("Unknown argument type = ${type.simpleName}")
        } as T?
    }


inline fun <reified T : Any> Bundle?.findArgument(key: String, getArgument: Bundle.() -> T?): T? =
    this?.let { if (it.containsKey(key)) it.getArgument() else null }