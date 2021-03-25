package kekmech.ru.bars.screen.main

import android.os.Bundle
import android.view.View
import kekmech.ru.bars.R
import kekmech.ru.bars.screen.main.elm.BarsEffect
import kekmech.ru.bars.screen.main.elm.BarsEvent
import kekmech.ru.bars.screen.main.elm.BarsEvent.Wish
import kekmech.ru.bars.screen.main.elm.BarsFeatureFactory
import kekmech.ru.bars.screen.main.elm.BarsState
import kekmech.ru.common_mvi.BaseFragment
import org.koin.android.ext.android.inject

class BarsFragment : BaseFragment<BarsEvent, BarsEffect, BarsState>() {

    override val initEvent: BarsEvent = Wish.Init
    override val layoutId: Int = R.layout.fragment_bars

    override fun createStore() = inject<BarsFeatureFactory>().value.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }

    override fun render(state: BarsState) {

    }

    override fun handleEffect(effect: BarsEffect) {

    }
}