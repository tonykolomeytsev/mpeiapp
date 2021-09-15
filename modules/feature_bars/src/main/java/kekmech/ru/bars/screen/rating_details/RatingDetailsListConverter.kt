package kekmech.ru.bars.screen.rating_details

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import androidx.annotation.StringRes
import kekmech.ru.bars.R
import kekmech.ru.bars.screen.main.util.DeclensionHelper
import kekmech.ru.bars.screen.rating_details.RatingDetailsFragment.Companion.ITEM_ID_SCIENCE
import kekmech.ru.bars.screen.rating_details.RatingDetailsFragment.Companion.ITEM_ID_SOCIAL
import kekmech.ru.bars.screen.rating_details.RatingDetailsFragment.Companion.ITEM_ID_STUDY
import kekmech.ru.bars.screen.rating_details.item.CompositeRatingItem
import kekmech.ru.common_android.getStringArray
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.coreui.items.TextItem
import kekmech.ru.domain_bars.dto.Rating

internal class RatingDetailsListConverter(
    private val context: Context,
) {

    fun map(rating: Rating): List<Any> =
        mutableListOf<Any>().apply {
            addComplexRatingHeaders(rating)

            addCompositeRatingItem(
                nameResId = R.string.bars_rating_study_name,
                value = rating.study.value,
                weight = rating.study.weight,
                progress = rating.study.let { it.value * it.weight } / rating.complex,
                itemId = ITEM_ID_STUDY
            )
            addCompositeRatingItem(
                nameResId = R.string.bars_rating_science_name,
                value = rating.science.value,
                weight = rating.science.weight,
                progress = rating.science.let { it.value * it.weight } / rating.complex,
                itemId = ITEM_ID_SCIENCE
            )
            addCompositeRatingItem(
                nameResId = R.string.bars_rating_social_name,
                value = rating.social.value,
                weight = rating.social.weight,
                progress = rating.social.let { it.value * it.weight } / rating.complex,
                itemId = ITEM_ID_SOCIAL
            )

            add(SpaceItem.VERTICAL_16)
            add(TextItem(
                text = "Полную информацию о формировании \n" +
                        "рейтинга,читайте в положении о БАРС \n" +
                        "на официальном сайте МЭИ.",
                styleResId = R.style.H7_Gray70,
                textGravity = Gravity.CENTER,
            ))
        }

    private fun MutableList<Any>.addComplexRatingHeaders(rating: Rating) {
        add(SpaceItem.VERTICAL_8)
        add(TextItem(
            textResId = R.string.bars_rating_complex_name,
            styleResId = R.style.H4_Gray70,
        ))

        val complexRatingPoints =
            DeclensionHelper.format(
                declensions = context.getStringArray(R.array.points_declensions),
                n = rating.complex.toLong()
            )
        add(TextItem(
            text = SpannableStringBuilder()
                .append(complexRatingPoints)
                .apply {
                    setSpan(
                        ForegroundColorSpan(context.getThemeColor(R.attr.colorActive)),
                        0,
                        rating.complex.toString().length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                },
            styleResId = R.style.H1
        ))
        add(SpaceItem.VERTICAL_16)
    }

    private fun MutableList<Any>.addCompositeRatingItem(
        @StringRes nameResId: Int,
        value: Int,
        weight: Float,
        progress: Float,
        itemId: Int,
    ) {
        val studyRatingPoints =
            DeclensionHelper.format(
                declensions = context.getStringArray(R.array.points_declensions),
                n = value.toLong()
            )
        add(CompositeRatingItem(
            nameResId = nameResId,
            value = SpannableStringBuilder()
                .append(studyRatingPoints)
                .apply {
                    setSpan(
                        ForegroundColorSpan(context.getThemeColor(R.attr.colorActive)),
                        0,
                        value.toString().length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                },
            weight = weight,
            progress = progress,
            itemId = itemId
        ))
    }
}