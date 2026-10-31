package kekmech.ru.ui_theme.typography

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontLoadingStrategy
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.res_fonts.R
import kekmech.ru.ui_theme.color.LightMpeixPalette
import kekmech.ru.ui_theme.theme.MpeixTheme

public val RobotoFontFamily: FontFamily = FontFamily(
    Font(
        resId = R.font.roboto_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal,
        loadingStrategy = FontLoadingStrategy.Async,
    ),
    Font(
        resId = R.font.roboto_medium,
        weight = FontWeight.Medium,
        style = FontStyle.Normal,
        loadingStrategy = FontLoadingStrategy.Async,
    ),
    Font(
        resId = R.font.roboto_bold,
        weight = FontWeight.Bold,
        style = FontStyle.Normal,
        loadingStrategy = FontLoadingStrategy.Async,
    ),
)

private val H1 = TextStyle(
    fontFamily = RobotoFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 38.sp,
)
private val H2 = TextStyle(
    fontFamily = RobotoFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 28.sp,
)
private val H3 = TextStyle(
    fontFamily = RobotoFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 23.sp,
)
private val H4 = TextStyle(
    fontFamily = RobotoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 22.sp,
)
private val H5 = TextStyle(
    fontFamily = RobotoFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 22.sp,
)
private val H6 = TextStyle(
    fontFamily = RobotoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 19.sp,
)
private val H7 = TextStyle(
    fontFamily = RobotoFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 19.sp,
)
private val H8 = TextStyle(
    fontFamily = RobotoFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp,
)
private val H8Medium = TextStyle(
    fontFamily = RobotoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp,
)
private val H9 = TextStyle(
    fontFamily = RobotoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 10.sp,
    lineHeight = 14.sp,
)


@Immutable
public class MpeixTypography internal constructor(
    public val headlineBig: TextStyle = H1,
    public val headlineSmall: TextStyle = H2,
    public val titleLarge: TextStyle = H3,
    public val titleMedium: TextStyle = H4,
    public val titleSmall: TextStyle = H6,
    public val bodyLarge: TextStyle = H5,
    public val bodyMedium: TextStyle = H7,
    public val bodySmall: TextStyle = H8,
    public val labelLarge: TextStyle = H4, // + ALL CAPS
    public val labelMedium: TextStyle = H8Medium,
    public val labelSmall: TextStyle = H9,
)

@Composable
@Preview(device = "spec:width=1920px,height=1080px,dpi=240")
@Suppress("LongMethod", "MagicNumber", "StringLiteralDuplication")
private fun TypographyPreview() {

    fun Modifier.drawDashedBorder(): Modifier =
        drawBehind {
            drawRoundRect(
                color = LightMpeixPalette.outline,
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                style = Stroke(
                    width = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(10f, 10f),
                        phase = 0f,
                    )
                ),
            )
        }

    val linePaddingModifier = Modifier.padding(bottom = 8.dp)

    MpeixTheme {
        Column(
            modifier = Modifier
                .background(
                    color = LightMpeixPalette.surface,
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .drawDashedBorder()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Headline Big",
                        style = MpeixTheme.typography.headlineBig,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Headline Small",
                        style = MpeixTheme.typography.headlineSmall,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Title Large",
                        style = MpeixTheme.typography.titleLarge,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Title Medium",
                        style = MpeixTheme.typography.titleMedium,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Title Small",
                        style = MpeixTheme.typography.titleSmall,
                        color = LightMpeixPalette.onSurface,
                    )
                }
                Spacer(Modifier.width(64.dp))
                Column {
                    Text(
                        text = "Крупные заголовки экранов",
                        style = MpeixTheme.typography.headlineBig,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Заголовки экранов и AlertDialog",
                        style = MpeixTheme.typography.headlineSmall,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Заголовки секция, TopAppBar",
                        style = MpeixTheme.typography.titleLarge,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "???",
                        style = MpeixTheme.typography.titleMedium,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Вкладки",
                        style = MpeixTheme.typography.titleSmall,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .drawDashedBorder()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Body Large",
                        style = MpeixTheme.typography.bodyLarge,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Body Medium",
                        style = MpeixTheme.typography.bodyMedium,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Body Small",
                        style = MpeixTheme.typography.bodySmall,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                }
                Spacer(Modifier.width(64.dp))
                Column {
                    Text(
                        text = "Текст по-умолчанию, ListItem, поля ввода",
                        style = MpeixTheme.typography.bodyLarge,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Вторичный текст, тексты диалогов, снекбаров",
                        style = MpeixTheme.typography.bodyMedium,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "???",
                        style = MpeixTheme.typography.bodySmall,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .drawDashedBorder()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Label Large".uppercase(),
                        style = MpeixTheme.typography.labelLarge,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Label Medium",
                        style = MpeixTheme.typography.labelMedium,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Label Small",
                        style = MpeixTheme.typography.labelSmall,
                        color = LightMpeixPalette.onSurface,
                    )
                }
                Spacer(Modifier.width(64.dp))
                Column {
                    Text(
                        text = "Кнопки + CAPS",
                        style = MpeixTheme.typography.labelLarge,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Подписи в NavigationBar",
                        style = MpeixTheme.typography.labelMedium,
                        color = LightMpeixPalette.onSurface,
                        modifier = linePaddingModifier,
                    )
                    Text(
                        text = "Мелкие сноски",
                        style = MpeixTheme.typography.labelSmall,
                        color = LightMpeixPalette.onSurface,
                    )
                }
            }
        }
    }

}
