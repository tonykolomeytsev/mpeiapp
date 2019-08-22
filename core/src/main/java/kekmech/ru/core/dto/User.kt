package kekmech.ru.core.dto

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User (
    @PrimaryKey var id: Int = 0,
    var firstLaunchFlag: Boolean? = null, // первый запуск
    var developerMode: Boolean? = null, // режим разработчика
    var groupName: String? = null, // номер группы
    var nightMode: Boolean? = null
) : RealmObject()