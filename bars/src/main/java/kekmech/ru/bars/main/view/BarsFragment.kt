package kekmech.ru.bars.main.view

import kekmech.ru.bars.R
import kekmech.ru.bars.main.BarsFragmentPresenter
import kekmech.ru.core.platform.BaseFragment
import javax.inject.Inject

class BarsFragment : BaseFragment<BarsFragmentPresenter, BarsFragmentView>(
    layoutId = R.layout.fragment_bars
), BarsFragmentView {

    @Inject
    override lateinit var presenter: BarsFragmentPresenter
}