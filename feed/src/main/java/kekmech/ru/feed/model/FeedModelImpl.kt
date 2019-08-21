package kekmech.ru.feed.model

import android.util.Log
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.User
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase
import kekmech.ru.core.usecases.LoadUserInfoUseCase
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.feed.items.CoupleItem
import kekmech.ru.feed.items.LunchItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScope
class FeedModelImpl @Inject constructor(
    val loadOffsetScheduleUseCase: LoadOffsetScheduleUseCase,
    val loadUserInfoUseCase: LoadUserInfoUseCase
) : FeedModel {

    override var scheduleInfoUpdateListener: (List<CoupleNative>) -> Unit = {}

    /**
     * Group number like "C-12-16"
     */
    override val groupNumber: String
        get() = loadUserInfoUseCase.execute().groupName ?: ""

    /**
     * Current week number
     */
    override val weekNumber: Int
        get() = 0 // FIXME научиться определять номер недели

    /**
     * Get couples for day
     * @param offset - 0 - today, 1 - yesterday etc.
     * @return return today's couples if offset == 0
     */
    override suspend fun getDayCouples(offset: Int, refresh: Boolean): List<BaseItem<*>> {
        return withContext(Dispatchers.IO) {
            val list = loadOffsetScheduleUseCase.execute(offset, refresh)
            list.map { couple ->
                when (couple.type) {
                    CoupleNative.LUNCH -> LunchItem(couple)
                    else -> CoupleItem(couple)
                }
            }
        }
    }

}