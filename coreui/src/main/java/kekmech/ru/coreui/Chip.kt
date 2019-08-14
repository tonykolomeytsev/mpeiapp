package kekmech.ru.coreui

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class Chip : LinearLayout {

    companion object {
        private val DEFAULT_TEXT_OPACITY_FACTOR = 0.87f
        private val DEFAULT_CHIP_OPACITY_FACTOR = 0.12f
        private val DEFAULT_ACCENT_COLOR_TEXT = Color.argb((255*DEFAULT_TEXT_OPACITY_FACTOR).toInt(), 0, 0, 0)
        private val DEFAULT_ACCENT_COLOR_CHIP = Color.argb((255*DEFAULT_CHIP_OPACITY_FACTOR).toInt(), 0, 0, 0)
    }

    private var accentColorText = DEFAULT_ACCENT_COLOR_TEXT
    private var accentColorChip = DEFAULT_ACCENT_COLOR_CHIP
    private val contentView: LinearLayout

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.item_chip_layout, null) as LinearLayout
        addView(contentView)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        parseAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        parseAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes){
        parseAttrs(attrs)
    }

    private fun parseAttrs(attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.Chip, 0, 0)
        val accentColor = typedArray.getColor(R.styleable.Chip_accentColor, DEFAULT_ACCENT_COLOR_TEXT)
        val text = typedArray.getString(R.styleable.Chip_text)
        typedArray.recycle()

        accentColorText = Color.argb(
            (Color.alpha(accentColor) * DEFAULT_TEXT_OPACITY_FACTOR).toInt(),
            Color.red(accentColor),
            Color.green(accentColor),
            Color.blue(accentColor))
        accentColorChip = Color.argb(
            60,/*(Color.alpha(accentColor) * DEFAULT_CHIP_OPACITY_FACTOR).toInt(),*/
            Color.red(accentColor),
            Color.green(accentColor),
            Color.blue(accentColor))
        val textView = contentView.findViewById<TextView>(R.id.textViewChipLayout)
        textView.setTextColor(accentColorText)
        textView.text = text
        contentView.background.setColorFilter(accentColorChip, PorterDuff.Mode.SRC_ATOP)

    }
}