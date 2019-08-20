package kekmech.ru.core.dto

import io.realm.RealmObject

open class User (
    var firstLaunchFlag: Boolean? = null, // первый запуск
    var developerMode: Boolean? = null // режим разработчика
) : RealmObject()