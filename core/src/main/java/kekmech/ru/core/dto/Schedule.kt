package kekmech.ru.core.dto

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Schedule(
    @PrimaryKey var id: Int = 0,
    var weekInfo: WeekInfo? = null,
    var coupleList: RealmList<CoupleNative>? = null
) : RealmObject()