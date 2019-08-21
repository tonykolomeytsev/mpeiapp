package kekmech.ru.core.dto

import io.realm.RealmList
import io.realm.RealmObject

open class Schedule(
    var weekInfo: WeekInfo? = null,
    var coupleList: RealmList<CoupleNative>? = null
) : RealmObject()