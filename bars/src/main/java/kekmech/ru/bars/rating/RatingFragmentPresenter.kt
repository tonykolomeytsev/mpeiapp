package kekmech.ru.bars.rating

import kekmech.ru.bars.main.model.BarsFragmentModel
import kekmech.ru.bars.rating.view.RatingFragmentView
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router

class RatingFragmentPresenter constructor(
    private val model: BarsFragmentModel,
    private val router: Router
): Presenter<RatingFragmentView>() {

    override fun onResume(view: RatingFragmentView) {
        super.onResume(view)
        view.onBackNav = router::popBackStack
        if (model.ratingDetails == null)
            router.popBackStack()
        else {
            view.setRating(model.ratingDetails!!)
        }
    }
}