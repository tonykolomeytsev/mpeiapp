package kekmech.ru.domain

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.FeedCarousel
import kekmech.ru.core.repositories.FeedRepository
import kekmech.ru.core.usecases.GetFeedCarouselUseCase

class GetFeedCarouselUseCaseImpl(
    private val feedRepository: FeedRepository
) : GetFeedCarouselUseCase {
    override fun invoke(): LiveData<FeedCarousel> {
        feedRepository.loadFreshCarousel()
        return feedRepository.feedCarousel
    }
}