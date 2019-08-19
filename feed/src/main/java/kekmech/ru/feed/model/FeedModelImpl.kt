package kekmech.ru.feed.model

import kekmech.ru.core.dto.Couple
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.core.scopes.FeedScope
import kekmech.ru.core.scopes.FragmentScope
import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase
import kekmech.ru.coreui.adapter.BaseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScope
class FeedModelImpl @Inject constructor(
//    val loadOffsetScheduleUseCase: LoadOffsetScheduleUseCase
) : FeedModel {


    /**
     * Group number like "C-12-16"
     */
    override val groupNumber: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    /**
     * Current week number
     */
    override val weekNumber: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    /**
     * Get couples for day
     * @param offset - 0 - today, 1 - yesterday etc.
     * @return return today's couples if offset == 0
     */
    override suspend fun getDayCouples(offset: Int): List<BaseItem<*>> {
        TODO("not implemented")
//        return withContext(Dispatchers.IO) {
//            loadOffsetScheduleUseCase.init(offset)
//            val couples: List<Couple> = loadOffsetScheduleUseCase.execute()
//            // TODO if couples is empty
//
//            emptyList<BaseItem<*>>()
//        }
    }

}