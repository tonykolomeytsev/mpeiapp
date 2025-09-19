package kekmech.ru.feature_bars_impl.data.mapper

import kekmech.ru.feature_bars_impl.data.model.RawRatingResponse
import kekmech.ru.feature_bars_impl.domain.CompositeRating
import kekmech.ru.feature_bars_impl.domain.Rating
import kekmech.ru.feature_bars_impl.domain.SocialCompositeRating

internal object RawToRatingResponseMapper {

    fun map(rawRatingResponse: RawRatingResponse): Rating =
        Rating(
            complex = rawRatingResponse.complexScore,
            study = CompositeRating(
                value = rawRatingResponse.studyScore,
                weight = rawRatingResponse.weights.studyScoreWeight,
            ),
            science = CompositeRating(
                value = rawRatingResponse.scienceScore,
                weight = rawRatingResponse.weights.scienceScoreWeight,
            ),
            social = SocialCompositeRating(
                value = rawRatingResponse.socialScore,
                weight = rawRatingResponse.weights.socialScoreWeight,
                sportValue = rawRatingResponse.sportScore,
                socialActivityValue = rawRatingResponse.socialActivityScore,
            )
        )
}
