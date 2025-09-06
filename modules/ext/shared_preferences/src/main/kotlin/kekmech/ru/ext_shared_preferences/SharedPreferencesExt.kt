package kekmech.ru.ext_shared_preferences

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

public sealed class SharedPreferencesDelegate<T>(public open val sharedPreferences: SharedPreferences) :
    ReadWriteProperty<Any, T>

public class StringPreference(
    preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: String = "",
) : SharedPreferencesDelegate<String>(preferences) {

    override fun getValue(thisRef: Any, property: KProperty<*>): String =
        sharedPreferences.getString(key, defaultValue)!!

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        sharedPreferences
            .edit()
            .putString(key, value)
            .apply()
    }
}

public class IntPreference(
    preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Int = 0,
) : SharedPreferencesDelegate<Int>(preferences) {

    override fun getValue(thisRef: Any, property: KProperty<*>): Int =
        sharedPreferences.getInt(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        sharedPreferences
            .edit()
            .putInt(key, value)
            .apply()
    }
}

public class BooleanPreference(
    preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Boolean = false,
) : SharedPreferencesDelegate<Boolean>(preferences) {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(key, value)
            .apply()
    }
}

public class TypedPreference<T>(
    preferences: SharedPreferences,
    private val key: String,
    private val toString: (T) -> String,
    private val fromString: (String) -> T,
    private val defaultValue: T,
) : SharedPreferencesDelegate<T>(preferences) {

    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        runCatching { fromString.invoke(sharedPreferences.getString(key, "")!!) }
            .getOrElse { defaultValue }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        runCatching {
            sharedPreferences
                .edit()
                .putString(key, toString.invoke(value))
                .apply()
        }
    }
}

public fun SharedPreferences.string(key: String, defaultValue: String = ""): StringPreference =
    StringPreference(this, key, defaultValue)

public fun SharedPreferences.int(key: String, defaultValue: Int = 0): IntPreference =
    IntPreference(this, key, defaultValue)

public fun SharedPreferences.boolean(key: String, defaultValue: Boolean = false): BooleanPreference =
    BooleanPreference(this, key, defaultValue)

public fun<T> SharedPreferences.typed(
    key: String,
    toString: (T) -> String,
    fromString: (String) -> T,
    defaultValue: T,
): TypedPreference<T> = TypedPreference(this, key, toString, fromString, defaultValue)
