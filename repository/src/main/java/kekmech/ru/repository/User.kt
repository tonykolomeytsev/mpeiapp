package kekmech.ru.repository

import io.realm.RealmObject

open class User (
    var firstLaunchFlag: Boolean? = null,
    var developerMode: Boolean? = null
) : RealmObject()