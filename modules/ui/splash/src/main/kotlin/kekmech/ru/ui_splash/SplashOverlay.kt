package kekmech.ru.ui_splash

import android.graphics.Color
import android.provider.Settings
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat

/**
 * Full-screen Compose splash drawn above the already inflated activity content.
 *
 * The system splash (see `Theme.Mpeix.SplashScreen`) is a plain black screen, so the handover
 * to this overlay and from it to the main screen has no flashes: both are black at the first frame
 * and the overlay fades out over the ready content.
 */
public object SplashOverlay {

    /**
     * Attach the overlay to [activity] content. Must be called after `setContentView`.
     *
     * @param logoRes drawable of the emblem
     * @param brandName localized brand name shown below the emblem
     * @param onFinished called after the overlay has faded out and was removed
     */
    public fun attach(
        activity: ComponentActivity,
        @DrawableRes logoRes: Int,
        brandName: String,
        onFinished: () -> Unit = {},
    ) {
        val content = activity.findViewById<ViewGroup>(android.R.id.content)
        val window = activity.window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        val wasLightStatusBars = insetsController.isAppearanceLightStatusBars
        val wasLightNavBars = insetsController.isAppearanceLightNavigationBars
        // Black background: light system bar icons are required while the overlay is shown
        insetsController.isAppearanceLightStatusBars = false
        insetsController.isAppearanceLightNavigationBars = false

        val animate = areAnimationsEnabled(activity)
        val overlay = ComposeView(activity).apply {
            setBackgroundColor(Color.BLACK)
            // swallow touches: the main screen below must not react while the splash is visible
            isClickable = true
            isFocusable = true
            importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        overlay.setContent {
            SplashAnimation(
                logo = painterResource(logoRes),
                brandName = brandName,
                animate = animate,
                onFinished = {
                    insetsController.isAppearanceLightStatusBars = wasLightStatusBars
                    insetsController.isAppearanceLightNavigationBars = wasLightNavBars
                    content.removeView(overlay)
                    onFinished()
                },
            )
        }
        content.addView(
            overlay,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT),
        )
    }

    /** `Settings.Global.ANIMATOR_DURATION_SCALE == 0` means "remove animations" (or reduce motion) */
    private fun areAnimationsEnabled(activity: ComponentActivity): Boolean =
        Settings.Global.getFloat(
            activity.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f,
        ) != 0f
}
