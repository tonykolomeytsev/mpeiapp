package kekmech.ru.domain_bars.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating(
    val complex: Int,
    val study: CompositeRating,
    val science: CompositeRating,
    val social: SocialCompositeRating,
) : Parcelable

@Parcelize
data class CompositeRating(
    val value: Int,
    val weight: Float,
) : Parcelable

@Parcelize
data class SocialCompositeRating(
    val value: Int,
    val weight: Float,
    val sportValue: Int,
    val socialActivityValue: Int,
) : Parcelable