package kekmech.ru.bars.rating.view

import androidx.lifecycle.LifecycleOwner
import kekmech.ru.core.dto.AcademicScore

interface RatingFragmentView : LifecycleOwner {
    var onBackNav: () -> Unit
    fun setRating(ratingDetails: AcademicScore.Rating)
}