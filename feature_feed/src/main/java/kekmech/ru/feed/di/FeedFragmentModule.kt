package kekmech.ru.feed.di

import kekmech.ru.feed.model.FeedModel
import kekmech.ru.feed.model.FeedModelImpl
import kekmech.ru.feed.FeedViewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val KoinFeedFragmentModule = module {
    // feed MVP pattern
    single { FeedViewModel(get(), get(), get(), get()) }
    single { FeedModelImpl(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) } bind FeedModel::class
}