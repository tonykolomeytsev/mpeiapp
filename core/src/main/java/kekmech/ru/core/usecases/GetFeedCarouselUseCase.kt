package kekmech.ru.core.usecases

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.FeedCarousel

interface GetFeedCarouselUseCase {
    operator fun invoke(): LiveData<FeedCarousel>
}