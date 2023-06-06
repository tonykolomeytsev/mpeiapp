package kekmech.ru.common_compose_theme.typography

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
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
import kekmech.ru.common_compose_theme.R
import kekmech.ru.common_compose_theme.color.LightMpeixPalette

val RobotoFontFamily: FontFamily = FontFamily(
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

object MpeixTypography {

    val Header1 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
    )
    val Header2 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
    )
    val Header3 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
    )
    val Header4 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal,
    )

    val ParagraphBig = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
    )
    val ParagraphNormal = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
    )
    val ParagraphBigAccent = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
    )
    val ParagraphNormalAccent = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
    )

    val LabelBig = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal,
    )
    val LabelNormal = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal,
    )
    val LabelMini = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 10.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal,
    )
}

val Typography = Typography(
    headlineLarge = MpeixTypography.Header1,
    headlineMedium = MpeixTypography.Header2,
    headlineSmall = MpeixTypography.Header3,
    titleLarge = MpeixTypography.Header4,
    titleMedium = MpeixTypography.ParagraphBigAccent,
    titleSmall = MpeixTypography.ParagraphNormalAccent,
    bodyLarge = MpeixTypography.ParagraphBig,
    bodyMedium = MpeixTypography.ParagraphNormal,
    bodySmall = MpeixTypography.LabelBig,
    labelLarge = MpeixTypography.LabelBig,
    labelMedium = MpeixTypography.LabelNormal,
    labelSmall = MpeixTypography.LabelMini,
)

@Composable
@Preview(device = "spec:width=1920px,height=1080px,dpi=240")
private fun TypographyPreview() {

    fun Modifier.drawDashedBorder(): Modifier =
        drawBehind {
            drawRoundRect(
                color = LightMpeixPalette.Outline,
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
                color = LightMpeixPalette.Surface,
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
                    style = MpeixTypography.Header1,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Header H2 28/36",
                    style = MpeixTypography.Header2,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Header H3 22/28",
                    style = MpeixTypography.Header3,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Header H4 20/24",
                    style = MpeixTypography.Header4,
                    color = LightMpeixPalette.Content,
                )
            }
            Spacer(Modifier.width(64.dp))
            Column {
                Text(
                    text = "Заголовки экранов",
                    style = MpeixTypography.Header1,
                    color = LightMpeixPalette.Content,
                    modifier =linePaddingModifier,
                )
                Text(
                    text = "Заголовки экранов",
                    style = MpeixTypography.Header2,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Заголовки экранов",
                    style = MpeixTypography.Header3,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Заголовки внутри блоков контента",
                    style = MpeixTypography.Header4,
                    color = LightMpeixPalette.Content,
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
                    style = MpeixTypography.ParagraphBig,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Paragraph Normal 14/20",
                    style = MpeixTypography.ParagraphNormal,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Paragraph Big Accent 16/24",
                    style = MpeixTypography.ParagraphBigAccent,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Paragraph Normal Accent 14/20",
                    style = MpeixTypography.ParagraphNormalAccent,
                    color = LightMpeixPalette.Content,
                )
            }
            Spacer(Modifier.width(64.dp))
            Column {
                Text(
                    text = "Текстовый контент, несущий много смысла",
                    style = MpeixTypography.ParagraphBig,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Короткие надписи внутри UI, подзаголовки внутри контента",
                    style = MpeixTypography.ParagraphNormal,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Текстовый контент, несущий много смысла",
                    style = MpeixTypography.ParagraphBigAccent,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Короткие надписи внутри UI, подзаголовки внутри контента",
                    style = MpeixTypography.ParagraphNormalAccent,
                    color = LightMpeixPalette.Content,
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
                    style = MpeixTypography.LabelBig,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Label Normal 12/16",
                    style = MpeixTypography.LabelNormal,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Label Mini 10/16",
                    style = MpeixTypography.LabelMini,
                    color = LightMpeixPalette.Content,
                )
            }
            Spacer(Modifier.width(64.dp))
            Column {
                Text(
                    text = "Кнопки, поля ввода, ячейки",
                    style = MpeixTypography.LabelBig,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Ячейки и короткие надписи в UI",
                    style = MpeixTypography.LabelNormal,
                    color = LightMpeixPalette.Content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Ячейки и короткие надписи в UI",
                    style = MpeixTypography.LabelMini,
                    color = LightMpeixPalette.Content,
                )
            }
        }
    }
}
