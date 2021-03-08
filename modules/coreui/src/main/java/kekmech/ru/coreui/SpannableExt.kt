package kekmech.ru.coreui

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TextAppearanceSpan
import android.view.View
import androidx.annotation.*
import kekmech.ru.common_android.getResColor
import kekmech.ru.common_android.getThemeColor

class SpannableScope(
    private val context: Context
) {

    private val wrappers = mutableListOf<Wrapper>()

    private fun Any.wrap(): Wrapper {
        return if (this is Wrapper) {
            this
        } else {
            val wrapper = Wrapper(toString())
            wrappers += wrapper
            wrapper
        }
    }

    fun res(@StringRes resId: Int) = context.resources.getString(resId).wrap()

    fun str(any: Any): Wrapper = any.wrap()

    fun str(vararg any: Any) = any.forEach { it.wrap() }

    fun space(count: Int = 1) {
        check(count > 0) { "Space length must be greater than zero!" }
        wrappers += Wrapper(String(CharArray(count) { ' ' }))
    }

    fun spannable(builder: SpannableScope.() -> Unit) = spannable(context, builder)

    infix fun Any.format(builder: SpannableScope.() -> Unit) {
        wrap().apply {
            val matchResults = "%[sd]".toRegex().findAll(string).toList()
            val necessaryArgsCount = matchResults.size
            val scope = SpannableScope(context)
            builder.invoke(scope)
            check(necessaryArgsCount == scope.wrappers.size) { "Args count mut be equal!" }
            if (matchResults.isNotEmpty()) {
                wrappers -= this
                var lastEndIndex = 0
                val scopeIterator = scope.wrappers.iterator()
                for (res in matchResults) {
                    if (res.range.first > 0) {
                        wrappers += Wrapper(string.subSequence(lastEndIndex until res.range.first))
                    }
                    wrappers += scopeIterator.next().let { Wrapper(it.string, it.spans) }
                    lastEndIndex = res.range.last + 1
                }
                if (lastEndIndex != string.lastIndex) {
                    wrappers += Wrapper(string.subSequence(lastEndIndex..string.lastIndex))
                }
            }
        }
    }

    infix fun Any.textAppearance(@StyleRes styleResId: Int): Wrapper =
        wrap().apply { spans += { TextAppearanceSpan(it, styleResId) } }

    infix fun Any.textStyle(typefaceTextStyle: Int): Wrapper =
        wrap().apply { spans += { StyleSpan(typefaceTextStyle) } }

    fun Any.bold() = textStyle(Typeface.BOLD)

    fun Any.normal() = textStyle(Typeface.NORMAL)

    infix fun Any.color(@ColorInt color: Int): Wrapper =
        wrap().apply { spans += { ForegroundColorSpan(color) } }

    infix fun Any.colorRes(@ColorRes colorResId: Int): Wrapper =
        wrap().apply { spans += { ForegroundColorSpan(it.getResColor(colorResId)) } }

    infix fun Any.colorAttr(@AttrRes colorAttrId: Int): Wrapper =
        wrap().apply { spans += { ForegroundColorSpan(it.getThemeColor(colorAttrId)) } }

    infix fun Any.onClick(onClickListener: () -> Unit): Wrapper =
        wrap().apply {
            spans += {
                object : ClickableSpan() {
                    private val defaultPaint = Paint()

                    override fun onClick(widget: View) = onClickListener()

                    override fun updateDrawState(ds: TextPaint) {
                        ds.style = defaultPaint.style
                    }
                }
            }
        }

    fun compile(): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        var startIndex = 0
        for ((text, spans) in wrappers) {
            val endIndex = startIndex + text.length
            builder.append(text)
            if (spans.isNotEmpty()) {
                spans.forEach { span ->
                    try {
                        builder.setSpan(
                            span(context),
                            startIndex,
                            endIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            startIndex += text.length
        }
        return builder
    }
}

data class Wrapper(val string: CharSequence, val spans: MutableList<(Context) -> Any> = mutableListOf())

fun spannable(context: Context, builder: SpannableScope.() -> Unit): SpannableStringBuilder {
    val scope = SpannableScope(context)
    builder.invoke(scope)
    return scope.compile()
}