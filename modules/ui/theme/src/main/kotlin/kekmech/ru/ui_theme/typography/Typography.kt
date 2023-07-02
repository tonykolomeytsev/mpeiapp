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

internal val RobotoFontFamily: FontFamily = FontFamily(
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

@Immutable
class MpeixTypography internal constructor(
    val header1: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
    ),
    val header2: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
    ),
    val header3: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
    ),
    val header4: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal,
    ),
    val paragraphBig: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
    ),
    val paragraphNormal: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
    ),
    val paragraphBigAccent: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
    ),
    val paragraphNormalAccent: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
    ),
    val labelBig: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal,
    ),
    val labelNormal: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal,
    ),
    val labelMini: TextStyle = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 10.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal,
    ),
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
                    text = "Header H1 32/38",
                    style = MpeixTheme.typography.header1,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Header H2 28/36",
                    style = MpeixTheme.typography.header2,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Header H3 22/28",
                    style = MpeixTheme.typography.header3,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Header H4 20/24",
                    style = MpeixTheme.typography.header4,
                    color = LightMpeixPalette.content,
                )
            }
            Spacer(Modifier.width(64.dp))
            Column {
                Text(
                    text = "Заголовки экранов",
                    style = MpeixTheme.typography.header1,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Заголовки экранов",
                    style = MpeixTheme.typography.header2,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Заголовки экранов",
                    style = MpeixTheme.typography.header3,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Заголовки внутри блоков контента",
                    style = MpeixTheme.typography.header4,
                    color = LightMpeixPalette.content,
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
                    text = "Paragraph Big 16/24",
                    style = MpeixTheme.typography.paragraphBig,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Paragraph Normal 14/20",
                    style = MpeixTheme.typography.paragraphNormal,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Paragraph Big Accent 16/24",
                    style = MpeixTheme.typography.paragraphBigAccent,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Paragraph Normal Accent 14/20",
                    style = MpeixTheme.typography.paragraphNormalAccent,
                    color = LightMpeixPalette.content,
                )
            }
            Spacer(Modifier.width(64.dp))
            Column {
                Text(
                    text = "Текстовый контент, несущий много смысла",
                    style = MpeixTheme.typography.paragraphBig,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Короткие надписи внутри UI, подзаголовки внутри контента",
                    style = MpeixTheme.typography.paragraphNormal,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Текстовый контент, несущий много смысла",
                    style = MpeixTheme.typography.paragraphBigAccent,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Короткие надписи внутри UI, подзаголовки внутри контента",
                    style = MpeixTheme.typography.paragraphNormalAccent,
                    color = LightMpeixPalette.content,
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
                    text = "Label Big 14/20",
                    style = MpeixTheme.typography.labelBig,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Label Normal 12/16",
                    style = MpeixTheme.typography.labelNormal,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Label Mini 10/16",
                    style = MpeixTheme.typography.labelMini,
                    color = LightMpeixPalette.content,
                )
            }
            Spacer(Modifier.width(64.dp))
            Column {
                Text(
                    text = "Кнопки, поля ввода, ячейки",
                    style = MpeixTheme.typography.labelBig,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Ячейки и короткие надписи в UI",
                    style = MpeixTheme.typography.labelNormal,
                    color = LightMpeixPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Ячейки и короткие надписи в UI",
                    style = MpeixTheme.typography.labelMini,
                    color = LightMpeixPalette.content,
                )
            }
        }
    }
}
