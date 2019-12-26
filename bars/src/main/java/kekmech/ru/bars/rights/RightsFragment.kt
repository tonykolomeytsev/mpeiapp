package kekmech.ru.bars.rights

import kekmech.ru.bars.R
import kekmech.ru.core.platform.BaseFragment
import org.koin.android.ext.android.inject

class RightsFragment : BaseFragment<RightsFragmentPresenter, RightsFragmentView>(
    layoutId = R.layout.fragment_rights
), RightsFragmentView {

    override val presenter: RightsFragmentPresenter by inject()
}