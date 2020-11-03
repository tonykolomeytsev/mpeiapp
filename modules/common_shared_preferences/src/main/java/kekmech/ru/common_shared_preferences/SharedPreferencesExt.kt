package kekmech.ru.common_shared_preferences

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class SharedPreferencesDelegate<T : Any>(open val sharedPreferences: SharedPreferences) : ReadWriteProperty<Any, T>

class StringPreference(
    preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: String = ""
) : SharedPreferencesDelegate<String>(preferences) {

    override fun getValue(thisRef: Any, property: KProperty<*>): String = sharedPreferences.getString(key, defaultValue)!!
    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        sharedPreferences
            .edit()
            .putString(key, value)
            .apply()
    }
}

class IntPreference(
    preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Int = 0
) : SharedPreferencesDelegate<Int>(preferences) {

    override fun getValue(thisRef: Any, property: KProperty<*>): Int = sharedPreferences.getInt(key, defaultValue)
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        sharedPreferences
            .edit()
            .putInt(key, value)
            .apply()
    }
}

class BooleanPreference(
    preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Boolean = false
) : SharedPreferencesDelegate<Boolean>(preferences) {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean = sharedPreferences.getBoolean(key, defaultValue)
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(key, value)
            .apply()
    }
}

fun SharedPreferences.string(key: String, defaultValue: String = "") = StringPreference(this, key, defaultValue)
fun SharedPreferences.int(key: String, defaultValue: Int = 0) = IntPreference(this, key, defaultValue)
fun SharedPreferences.boolean(key: String, defaultValue: Boolean = false) = BooleanPreference(this, key, defaultValue)