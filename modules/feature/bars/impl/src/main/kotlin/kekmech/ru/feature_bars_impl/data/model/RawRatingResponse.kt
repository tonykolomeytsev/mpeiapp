package kekmech.ru.feature_bars_impl.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RawRatingResponse(
    @SerialName("complexScore")
    val complexScore: Int,

    @SerialName("studyScore")
    val studyScore: Int,

    @SerialName("scienceScore")
    val scienceScore: Int,

    @SerialName("socialScore")
    val socialScore: Int,

    @SerialName("sportScore")
    val sportScore: Int,

    @SerialName("socialActivityScore")
    val socialActivityScore: Int,

    @SerialName("weights")
    val weights: RawRatingWeights,
)

@Serializable
internal data class RawRatingWeights(
    @SerialName("studyScoreWeight")
    val studyScoreWeight: Float,

    @SerialName("scienceScoreWeight")
    val scienceScoreWeight: Float,

    @SerialName("socialScoreWeight")
    val socialScoreWeight: Float,
)