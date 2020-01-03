package kekmech.ru.mainscreen

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.Window
import androidx.annotation.RequiresApi
import kekmech.ru.coreui.Resources

@RequiresApi(api = Build.VERSION_CODES.M)
class NavigationBarPainter {
    fun setColorNavigationBar(context: Context, window: Window, colorId: Int) {
        val metrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(metrics)

        val dimDrawable = GradientDrawable()
        // ...customize your dim effect here

        val navigationBarDrawable = GradientDrawable()
        navigationBarDrawable.shape = GradientDrawable.RECTANGLE
        navigationBarDrawable.setColor(Resources.getColor(context, colorId))

        val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)

        val windowBackground = LayerDrawable(layers)
        windowBackground.setLayerInsetTop(1, metrics.heightPixels)

        window.setBackgroundDrawable(windowBackground)
    }
}