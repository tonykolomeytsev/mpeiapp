package kekmech.ru.bars.main

import kekmech.ru.bars.R
import kekmech.ru.core.platform.BaseFragment
import javax.inject.Inject

class BarsFragment : BaseFragment<BarsFragmentPresenter, BarsFragmentView>(
    layoutId = R.layout.fragment_bars
), BarsFragmentView {

    @Inject
    override lateinit var presenter: BarsFragmentPresenter
}