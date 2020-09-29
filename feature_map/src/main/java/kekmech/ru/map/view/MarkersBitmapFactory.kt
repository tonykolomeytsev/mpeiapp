package kekmech.ru.map.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.example.map.R
import kekmech.ru.common_android.dpToPx
import kekmech.ru.common_android.getResColor
import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.domain_map.dto.MarkerType

class MarkersBitmapFactory(
    context: Context
) {
    private val iconHeight = context.resources.dpToPx(32)
    private val iconWidth = context.resources.dpToPx(32)
    private val backgroundDrawable = VectorDrawableCompat.create(context.resources, R.drawable.ic_place_32px, null)!!.apply {
        setBounds(0, 0, iconWidth, iconHeight)
    }
    private val bitmap = Bitmap.createBitmap(iconWidth, iconHeight, Bitmap.Config.ARGB_8888)
    private val pinkColor = context.getResColor(R.color.colorPink)
    private val greenColor = context.getResColor(R.color.colorGreen)
    private val blueColor = context.getResColor(R.color.colorMain)
    private val grayColor = context.getResColor(R.color.colorGray70)
    private val whiteColor = context.getResColor(R.color.colorWhite)
    private val scale = context.resources.displayMetrics.density
    private val paint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        isDither = true
        textAlign = Paint.Align.CENTER
        textSize = 12 * scale
        color = whiteColor
    }
    private val decorEmojis = mapOf<String, VectorDrawableCompat>(
        ":stadium:" to VectorDrawableCompat.create(context.resources, R.drawable.ic_marker, null)!!
    )

    fun getBitmap(mapMarker: MapMarker): Bitmap {
        val canvas = Canvas(bitmap)
        backgroundDrawable.setTint(getTintColor(mapMarker.type))
        backgroundDrawable.draw(canvas)
        drawDecor(mapMarker, canvas)
        return bitmap
    }

    private fun getTintColor(markerType: MarkerType) = when (markerType) {
        MarkerType.FOOD -> pinkColor
        MarkerType.BUILDING -> blueColor
        MarkerType.HOSTEL -> greenColor
        else -> grayColor
    }

    private fun drawDecor(marker: MapMarker, canvas: Canvas) {
        when (marker.type) {
            MarkerType.BUILDING -> {
                val icon = "К"//marker.icon
                if (icon.length == 1) {
                    canvas.drawText(icon, iconWidth / 2f, iconHeight / 1.8f, paint)
                }
            }
            else -> Unit
        }
    }
}