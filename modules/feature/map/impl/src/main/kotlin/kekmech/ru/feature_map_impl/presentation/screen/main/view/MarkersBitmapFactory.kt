@file:Suppress("MagicNumber,MaxLineLength")

package kekmech.ru.feature_map_impl.presentation.screen.main.view

import android.content.Context
import android.graphics.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import kekmech.ru.ext_android.dpToPx
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_map_api.domain.model.MarkerType
import kekmech.ru.feature_map_impl.R
import kekmech.ru.lib_emoji.EmojiProvider
import kekmech.ru.res_fonts.R as FontsR

private const val ICON_SIZE_DP = 32
private const val TEXT_SIZE_SP = 12
private const val DP_8 = 8

// Markers look the same in both themes, so the colors are fixed
private const val PINK_COLOR = 0xFFFF505C.toInt()
private const val GREEN_COLOR = 0xFF578B0F.toInt()
private const val BLUE_COLOR = 0xFF587FC7.toInt()
private const val PURPLE_COLOR = 0xFF673AB7.toInt()
private const val TEAL_COLOR = 0xFF009688.toInt()
private const val WHITE_COLOR = 0xFFFFFFFF.toInt()

@Suppress("UnsafeCallOnNullableType")
internal class MarkersBitmapFactory(
    context: Context,
    private val emojiProvider: EmojiProvider,
) {

    private val iconHeight = context.resources.dpToPx(ICON_SIZE_DP)
    private val iconWidth = context.resources.dpToPx(ICON_SIZE_DP)
    private val dp2 = context.resources.dpToPx(2)
    private val dp8 = context.resources.dpToPx(DP_8).toFloat()
    private val defaultDrawable = VectorDrawableCompat
        .create(context.resources, R.drawable.ic_place_32px, null)!!.apply {
            setBounds(0, 0, iconWidth, iconHeight)
        }
    private val defaultFoodDecor = VectorDrawableCompat
        .create(context.resources, R.drawable.ic_restaurant_24px, null)!!.apply {
            setBounds(0, 0, iconWidth / 2, iconHeight / 2)
        }
    private val defaultHostelsDecor = VectorDrawableCompat
        .create(context.resources, R.drawable.ic_hotel_24px, null)!!.apply {
            setBounds(0, 0, iconWidth / 2, iconHeight / 2)
        }
    private val defaultOtherDecor = VectorDrawableCompat
        .create(context.resources, R.drawable.ic_star_rate_24px, null)!!.apply {
            setBounds(0, 0, iconWidth / 2, iconHeight / 2)
        }
    private val defaultStructureDecor = VectorDrawableCompat
        .create(context.resources, R.drawable.ic_school_24px, null)!!.apply {
            setBounds(0, 0, iconWidth / 2, iconHeight / 2)
        }
    private val font = ResourcesCompat.getFont(context, FontsR.font.roboto_medium)
    private val bitmap = Bitmap.createBitmap(iconWidth, iconHeight, Bitmap.Config.ARGB_8888)
    private val scale = context.resources.displayMetrics.density
    private val paint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        isDither = true
        textAlign = Paint.Align.CENTER
        textSize = TEXT_SIZE_SP * scale
        color = WHITE_COLOR
        typeface = font
    }

    fun getBitmap(mapMarker: MapMarker): Bitmap {
        val emoji = emojiProvider.provideEmoji(mapMarker.icon.orEmpty())
        if (emoji != null) {
            emoji.setBounds(0, 0, iconWidth, iconHeight)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            emoji.draw(canvas)
            return bitmap
        }

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        defaultDrawable.setTint(getTintColor(mapMarker.type))
        defaultDrawable.draw(canvas)
        drawDecor(mapMarker, canvas)
        return bitmap
    }

    private fun getTintColor(markerType: MarkerType) = when (markerType) {
        MarkerType.FOOD -> PINK_COLOR
        MarkerType.BUILDING -> BLUE_COLOR
        MarkerType.HOSTEL -> GREEN_COLOR
        MarkerType.OTHER -> PURPLE_COLOR
        else -> TEAL_COLOR
    }

    private fun drawDecor(marker: MapMarker, canvas: Canvas) {
        when (marker.type) {
            MarkerType.BUILDING -> {
                val icon = marker.icon.orEmpty()
                if (icon.length == 1) {
                    canvas.drawText(icon, iconWidth / 2f, iconHeight / 2f + dp2, paint)
                }
            }
            MarkerType.FOOD -> {
                defaultFoodDecor.setTint(WHITE_COLOR)
                canvas.withTranslation(x = dp8, y = dp8 - dp2) {
                    defaultFoodDecor.draw(canvas)
                }
            }
            MarkerType.HOSTEL -> {
                defaultHostelsDecor.setTint(WHITE_COLOR)
                canvas.withTranslation(x = dp8, y = dp8 - dp2) {
                    defaultHostelsDecor.draw(canvas)
                }
            }
            MarkerType.OTHER -> {
                defaultOtherDecor.setTint(WHITE_COLOR)
                canvas.withTranslation(x = dp8, y = dp8 - dp2) {
                    defaultOtherDecor.draw(canvas)
                }
            }
            MarkerType.STRUCTURE -> {
                defaultStructureDecor.setTint(WHITE_COLOR)
                canvas.withTranslation(x = dp8, y = dp8 - dp2) {
                    defaultStructureDecor.draw(canvas)
                }
            }
            else -> Unit
        }
    }
}
