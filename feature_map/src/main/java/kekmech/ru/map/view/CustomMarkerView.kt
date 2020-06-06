package kekmech.ru.map.view

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.example.map.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kekmech.ru.coreui.Resources

class CustomMarkerView(context: Context, layoutId: Int, viewBinder: (View) -> Unit) : FrameLayout(context) {

    init {
        View.inflate(context, layoutId, this)
        viewBinder(this)
        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
    }

    fun getMarkerIcon(): BitmapDescriptor? {
        layout(0, 0, measuredWidth, measuredHeight)
        isDrawingCacheEnabled = true
        invalidate()
        buildDrawingCache(false)
        return BitmapDescriptorFactory.fromBitmap(drawingCache)
    }
}