package kekmech.ru.domain_bars

import kekmech.ru.domain_bars.dto.CompositeRating
import kekmech.ru.domain_bars.dto.Rating
import kekmech.ru.domain_bars.dto.RawRatingResponse
import kekmech.ru.domain_bars.dto.SocialCompositeRating

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
