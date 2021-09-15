package kekmech.ru.domain_bars.dto

data class Rating(
    val complex: Int,
    val study: CompositeRating,
    val science: CompositeRating,
    val social: SocialCompositeRating,
)

data class CompositeRating(
    val value: Int,
    val weight: Float,
)

data class SocialCompositeRating(
    val value: Int,
    val weight: Float,
    val sportValue: Int,
    val socialActivityValue: Int,
)