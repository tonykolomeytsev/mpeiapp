package kekmech.ru.bars.rights

import kekmech.ru.bars.R
import kekmech.ru.core.platform.BaseFragment
import javax.inject.Inject

class RightsFragment : BaseFragment<RightsFragmentPresenter, RightsFragmentView>(
    layoutId = R.layout.fragment_rights
), RightsFragmentView {
    @Inject
    override lateinit var presenter: RightsFragmentPresenter
}