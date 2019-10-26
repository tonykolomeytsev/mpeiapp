package com.example.map.view

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.example.map.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kekmech.ru.coreui.Resources

private class CustomMarkerView(root: ViewGroup, text: String?, isSelected: Boolean) : FrameLayout(root.context) {
    private var mTitle: TextView

    init {
        View.inflate(context, R.layout.item_placemark_building, this)
        mTitle = findViewById(R.id.textViewPlacemarkTitle)
        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        mTitle.text = text
        if (isSelected) {
            mTitle.backgroundTintList = ColorStateList.valueOf(Resources.getColor(context, R.color.colorPrimary))
            mTitle.setTextColor(Resources.getColor(context, R.color.colorWhite))
        } else {
            mTitle.backgroundTintList = ColorStateList.valueOf(Resources.getColor(context, R.color.colorBlackTransparent))
            mTitle.setTextColor(Resources.getColor(context, R.color.colorWhite))
        }
    }

    companion object {
        fun getMarkerIcon(root: ViewGroup, text: String?, isSelected: Boolean): BitmapDescriptor? {
            val markerView = CustomMarkerView(root, text, isSelected)
            markerView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)
            markerView.isDrawingCacheEnabled = true
            markerView.invalidate()
            markerView.buildDrawingCache(false)
            return BitmapDescriptorFactory.fromBitmap(markerView.drawingCache)
        }
    }
}