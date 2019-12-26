package kekmech.ru.domain

import kekmech.ru.core.gateways.PicassoFirebaseInstance
import kekmech.ru.core.repositories.FeedRepository
import kekmech.ru.core.usecases.GetPicassoInstanceUseCase

class GetPicassoInstanceUseCaseImpl(
    private val feedRepository: FeedRepository
) : GetPicassoInstanceUseCase {
    override fun invoke(): PicassoFirebaseInstance {
        return feedRepository.picassoInstance
    }
}