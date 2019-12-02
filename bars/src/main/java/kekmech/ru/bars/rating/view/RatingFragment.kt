package kekmech.ru.bars.rating.view

import kekmech.ru.bars.R
import kekmech.ru.bars.rating.RatingFragmentPresenter
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_bars_details.*
import kotlinx.android.synthetic.main.fragment_bars_details.toolbar
import kotlinx.android.synthetic.main.fragment_bars_rating.*
import javax.inject.Inject

class RatingFragment : BaseFragment<RatingFragmentPresenter, RatingFragmentView>(
    R.layout.fragment_bars_rating
), RatingFragmentView {

    override var onBackNav: () -> Unit = {}
    @Inject
    override lateinit var presenter: RatingFragmentPresenter

    override fun onResume() {
        super.onResume()
        toolbar.setNavigationOnClickListener {
            onBackNav()
        }
    }

    override fun setRating(ratingDetails: AcademicScore.Rating) {
        textViewRatingTotal.text = ratingDetails.total.toString()
        textViewRatingStudy.text = ratingDetails.study.toString()
        textViewRatingScience.text = ratingDetails.science.toString()
        textViewRatingSocial.text = ratingDetails.social_total.toString()
        textViewRatingSport.text = ratingDetails.social_sport.toString()
        textViewRatingActivity.text = ratingDetails.social_act.toString()
    }
}