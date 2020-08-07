package kekmech.ru.coreui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.view_progress_button.view.*

private const val LOOP_TIME = 1000L

class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var isLoading: Boolean = false
    var text: String? = null
        set(value) {
            field = value
            if (!isLoading) progressButton.text = value
        }

    init {
        View.inflate(context, R.layout.view_progress_button, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton)
        text = typedArray.getString(R.styleable.ProgressButton_text)
        isEnabled = typedArray.getBoolean(R.styleable.ProgressButton_enabled, true)
        typedArray.recycle()
    }

    private val dots = listOf(dot1, dot2, dot3)

    override fun setEnabled(enabled: Boolean) {
        progressButton.isEnabled = enabled
    }

    override fun setOnClickListener(l: OnClickListener?) {
        progressButton.setOnClickListener { view ->
            if (!isLoading) l?.onClick(view)
        }
    }

    fun setText(@StringRes textRes: Int) {
        text = context.getString(textRes)
    }

    fun setLoading(isLoading: Boolean) {
        setDotsVisibility(isLoading)
        if (!this.isLoading && isLoading) {
            startLoadingAnimation()
        }
        if (this.isLoading && !isLoading) {
            stopLoadingAnimation()
        }
        this.isLoading = isLoading
    }

    fun isLoading() = isLoading

    private fun setDotsVisibility(isVisible: Boolean) {
        progressButton.text = text.takeIf { !isVisible }
        dots.forEach { it.visibility = if (isVisible) VISIBLE else GONE }
    }

    private fun stopLoadingAnimation() {
        dots.forEach { it.animate().cancel() }
    }

    private fun startLoadingAnimation() {
        dots.forEach { it.alpha = 0f }
        dots.forEachIndexed { i, dot -> animateDotFadeIn(dot, LOOP_TIME / dots.size * i) }
    }

    private fun animateDotFadeIn(dot: View, delay: Long) {
        dot.alpha = 0f
        dot.animate()
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setStartDelay(delay)
            .alpha(1f)
            .setDuration(LOOP_TIME / 2)
            .withEndAction { animateDotFadeOut(dot) }
            .start()
    }

    private fun animateDotFadeOut(dot: View) {
        dot.alpha = 1f
        dot.animate()
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setStartDelay(0L)
            .alpha(0f)
            .setDuration(LOOP_TIME / 2)
            .withEndAction { animateDotFadeIn(dot, 0L) }
            .start()
    }
}