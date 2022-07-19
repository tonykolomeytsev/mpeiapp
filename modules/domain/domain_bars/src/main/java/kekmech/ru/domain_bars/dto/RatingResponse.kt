package kekmech.ru.domain_bars.dto

import java.io.Serializable

data class Rating(
    val complex: Int,
    val study: CompositeRating,
    val science: CompositeRating,
    val social: SocialCompositeRating,
) : Serializable

data class CompositeRating(
    val value: Int,
    val weight: Float,
) : Serializable

data class SocialCompositeRating(
    val value: Int,
    val weight: Float,
    val sportValue: Int,
    val socialActivityValue: Int,
) : Serializable