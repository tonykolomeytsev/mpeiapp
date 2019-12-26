package kekmech.ru.core.repositories

import androidx.lifecycle.MutableLiveData
import kekmech.ru.core.dto.FeedCarousel
import kekmech.ru.core.gateways.PicassoFirebaseInstance

interface FeedRepository {
    val picassoInstance: PicassoFirebaseInstance
    val feedCarousel: MutableLiveData<FeedCarousel>

    fun loadFreshCarousel()
}