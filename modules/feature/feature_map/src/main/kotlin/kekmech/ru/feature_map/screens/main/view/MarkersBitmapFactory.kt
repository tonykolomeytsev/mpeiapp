@file:Suppress("MagicNumber,MaxLineLength")

package kekmech.ru.feature_map.screens.main.view

import android.content.Context
import android.graphics.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import kekmech.ru.common_android.dpToPx
import kekmech.ru.common_android.getResColor
import kekmech.ru.common_emoji.EmojiProvider
import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.domain_map.dto.MarkerType
import kekmech.ru.feature_map.R
import kekmech.ru.coreui.R as coreui_R

private const val ICON_SIZE_DP = 32
private const val TEXT_SIZE_SP = 12
private const val DP_8 = 8

internal class MarkersBitmapFactory(
    context: Context,
    private val emojiProvider: EmojiProvider
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
    private val font = ResourcesCompat.getFont(context, coreui_R.font.roboto_medium)
    private val bitmap = Bitmap.createBitmap(iconWidth, iconHeight, Bitmap.Config.ARGB_8888)
    private val pinkColor = context.getResColor(coreui_R.color.colorPink)
    private val greenColor = context.getResColor(coreui_R.color.colorGreen)
    private val blueColor = context.getResColor(coreui_R.color.colorMain)
    // private val grayColor = context.getResColor(coreui_R.color.colorGray70)
    private val purpleColor = context.getResColor(coreui_R.color.colorPurple)
    private val tealColor = context.getResColor(coreui_R.color.colorTeal)
    private val whiteColor = context.getResColor(coreui_R.color.colorWhite)
    private val scale = context.resources.displayMetrics.density
    private val paint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        isDither = true
        textAlign = Paint.Align.CENTER
        textSize = TEXT_SIZE_SP * scale
        color = whiteColor
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
        MarkerType.FOOD -> pinkColor
        MarkerType.BUILDING -> blueColor
        MarkerType.HOSTEL -> greenColor
        MarkerType.OTHER -> purpleColor
        else -> tealColor
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
                defaultFoodDecor.setTint(whiteColor)
                canvas.withTranslation(x = dp8, y = dp8 - dp2) {
                    defaultFoodDecor.draw(canvas)
                }
            }
            MarkerType.HOSTEL -> {
                defaultHostelsDecor.setTint(whiteColor)
                canvas.withTranslation(x = dp8, y = dp8 - dp2) {
                    defaultHostelsDecor.draw(canvas)
                }
            }
            MarkerType.OTHER -> {
                defaultOtherDecor.setTint(whiteColor)
                canvas.withTranslation(x = dp8, y = dp8 - dp2) {
                    defaultOtherDecor.draw(canvas)
                }
            }
            MarkerType.STRUCTURE -> {
                defaultStructureDecor.setTint(whiteColor)
                canvas.withTranslation(x = dp8, y = dp8 - dp2) {
                    defaultStructureDecor.draw(canvas)
                }
            }
            else -> Unit
        }
    }
}
