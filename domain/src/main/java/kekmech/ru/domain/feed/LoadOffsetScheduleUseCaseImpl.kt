package kekmech.ru.domain.feed

import kekmech.ru.core.dto.Couple
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.core.scopes.FeedScope
import kekmech.ru.core.scopes.FragmentScope
import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase
import javax.inject.Inject

@ActivityScope
class LoadOffsetScheduleUseCaseImpl @Inject constructor() : LoadOffsetScheduleUseCase {
    override fun init(i: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun execute(): List<Couple> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}