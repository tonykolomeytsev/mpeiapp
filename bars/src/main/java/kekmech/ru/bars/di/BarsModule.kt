package kekmech.ru.bars.di

import kekmech.ru.bars.details.BarsDetailsFragmentPresenter
import kekmech.ru.bars.main.BarsFragmentPresenter
import kekmech.ru.bars.main.model.BarsFragmentModel
import kekmech.ru.bars.main.model.BarsFragmentModelImpl
import kekmech.ru.bars.rating.RatingFragmentPresenter
import kekmech.ru.bars.rights.RightsFragmentPresenter
import org.koin.dsl.bind
import org.koin.dsl.module

val KoinBarsModule = module {
    // bars fragment MVP
    single { BarsFragmentPresenter(get(), get(), get(), get()) }
    single { BarsFragmentModelImpl(get(), get(), get(), get(), get(), get(), get(), get()) } bind BarsFragmentModel::class

    // Rights fragment MVP
    single { RightsFragmentPresenter() }

    // details fragment MVP
    single { BarsDetailsFragmentPresenter(get(), get()) }

    // rating fragment MVP
    single { RatingFragmentPresenter(get(), get()) }
}