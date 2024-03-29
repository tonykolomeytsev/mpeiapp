package kekmech.ru.feature_bars_impl.data.dto

internal data class RawRatingResponse(
    val complexScore: Int,
    val studyScore: Int,
    val scienceScore: Int,
    val socialScore: Int,
    val sportScore: Int,
    val socialActivityScore: Int,
    val weights: RawRatingWeights,
)

internal data class RawRatingWeights(
    val studyScoreWeight: Float,
    val scienceScoreWeight: Float,
    val socialScoreWeight: Float,
)
