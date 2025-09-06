package kekmech.ru.ui_theme.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ui_theme.typography.RobotoFontFamily

@Immutable
public object MpeixColors {

    public val Primary90: Color = Color(0xFFDBE6FA)
    public val Primary60: Color = Color(0xFF4C83E8)
    public val Primary50: Color = Color(0xFF1D62E2)
    public val Secondary90: Color = Color(0xFFD1EDFA)
    public val Secondary60: Color = Color(0xFF4BB7E9)
    public val Secondary30: Color = Color(0xFF105F84)
    public val Tertiary50: Color = Color(0xFF1CE39D)
    public val Neutral100: Color = Color(0xFFFFFFFF)
    public val Neutral98: Color = Color(0xFFF6F7F9)
    public val Neutral97: Color = Color(0xFFF3F5F7)
    public val Neutral95: Color = Color(0xFFF0F2F5)
    public val Neutral93: Color = Color(0xFFE3E7ED)
    public val Neutral90: Color = Color(0xFFE0E4EB)
    public val Neutral85: Color = Color(0xFFD1D7E0)
    public val Neutral80: Color = Color(0xFFC2CAD6)
    public val Neutral70: Color = Color(0xFFA3AFC2)
    public val Neutral60: Color = Color(0xFF8494AE)
    public val Neutral40: Color = Color(0xFF51617B)
    public val Neutral35: Color = Color(0xFF3B4759)
    public val Neutral30: Color = Color(0xFF333D4D)
    public val Neutral25: Color = Color(0xFF2A3340)
    public val Neutral20: Color = Color(0xFF232A35)
    public val Neutral10: Color = Color(0xFF14181F)
    public val ClassesTypeLecture: Color = Color(0xFF16B37C)
    public val ClassesTypePractice: Color = Color(0xFFE8BC4C)
    public val ClassesTypeLab: Color = Color(0xFFE864AB)
}

@Suppress("LongParameterList")
@Immutable
public class MpeixPalette internal constructor(
    public val primary: Color,
    public val secondary: Color,
    public val tertiary: Color,
    public val background: Color,
    public val surface: Color,
    public val surfacePlus1: Color,
    public val surfacePlus2: Color,
    public val surfacePlus3: Color,
    public val shimmer: Color,
    public val primaryContainer: Color,
    public val secondaryContainer: Color,
    public val content: Color,
    public val contentAccent: Color,
    public val contentVariant: Color,
    public val contentDisabled: Color,
    public val outline: Color,
    public val classesTypeLecture: Color = MpeixColors.ClassesTypeLecture,
    public val classesTypePractice: Color = MpeixColors.ClassesTypePractice,
    public val classesTypeLab: Color = MpeixColors.ClassesTypeLab,
)

public val LightMpeixPalette: MpeixPalette = MpeixPalette(
    primary = MpeixColors.Primary60,
    secondary = MpeixColors.Secondary60,
    tertiary = MpeixColors.Tertiary50,
    background = MpeixColors.Neutral97,
    surface = MpeixColors.Neutral100,
    surfacePlus1 = MpeixColors.Neutral98,
    surfacePlus2 = MpeixColors.Neutral95,
    surfacePlus3 = MpeixColors.Neutral93,
    shimmer = MpeixColors.Neutral85,
    primaryContainer = MpeixColors.Primary90,
    secondaryContainer = MpeixColors.Secondary90,
    content = MpeixColors.Neutral20,
    contentAccent = MpeixColors.Neutral100,
    contentVariant = MpeixColors.Neutral40,
    contentDisabled = MpeixColors.Neutral60,
    outline = MpeixColors.Neutral80,
)

public val DarkMpeixPalette: MpeixPalette = MpeixPalette(
    primary = MpeixColors.Primary60,
    secondary = MpeixColors.Secondary60,
    tertiary = MpeixColors.Tertiary50,
    background = MpeixColors.Neutral10,
    surface = MpeixColors.Neutral20,
    surfacePlus1 = MpeixColors.Neutral25,
    surfacePlus2 = MpeixColors.Neutral30,
    surfacePlus3 = MpeixColors.Neutral35,
    shimmer = MpeixColors.Neutral25,
    primaryContainer = MpeixColors.Primary50,
    secondaryContainer = MpeixColors.Secondary30,
    content = MpeixColors.Neutral90,
    contentAccent = MpeixColors.Neutral100,
    contentVariant = MpeixColors.Neutral80,
    contentDisabled = MpeixColors.Neutral70,
    outline = MpeixColors.Neutral60,
)

@Composable
@Preview(device = "spec:width=1920px,height=1080px,dpi=240")
@Suppress("LongMethod")
private fun PalettePreview(
    @PreviewParameter(MpeixPaletteParameterProvider::class) palette: MpeixPalette,
) {
    val colorItem: @Composable (
        name: String,
        backgroundColor: Color,
        textColor: Color,
    ) -> Unit = { name, backgroundColor, textColor ->
        Box(
            modifier = Modifier
                .size(width = 96.dp, height = 48.dp)
                .background(backgroundColor)
                .padding(horizontal = 4.dp, vertical = 2.dp),
        ) {
            Text(
                text = name,
                color = textColor,
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(
                color = palette.surface,
                shape = RoundedCornerShape(24.dp),
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Color Palette",
            fontFamily = RobotoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            color = palette.content,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(Modifier.padding(bottom = 12.dp)) {
            colorItem("Background", palette.background, palette.content)
            colorItem("Surface", palette.surface, palette.content)
            colorItem("Surface + 1", palette.surfacePlus1, palette.content)
            colorItem("Surface + 2", palette.surfacePlus2, palette.content)
            colorItem("Surface + 3", palette.surfacePlus3, palette.content)
            colorItem("Shimmer", palette.surfacePlus3, palette.content)
        }
        Row(Modifier.padding(bottom = 12.dp)) {
            colorItem("Primary", palette.primary, palette.contentAccent)
            colorItem("Secondary", palette.secondary, palette.contentAccent)
            colorItem("Tertiary", palette.tertiary, palette.contentAccent)
            colorItem("ContentAccent", palette.contentAccent, MpeixColors.Neutral10)
        }
        Row(Modifier.padding(bottom = 12.dp)) {
            colorItem("Primary\nContainer", palette.primaryContainer, palette.content)
            colorItem("Secondary\nContainer", palette.secondaryContainer, palette.content)
            colorItem("Content", palette.content, palette.surface)
            colorItem("ContentVariant", palette.contentVariant, palette.surface)
            colorItem("Content\nDisabled", palette.contentDisabled, palette.contentAccent)
            colorItem("Outline", palette.outline, palette.content)
        }
        Row {
            colorItem(
                "ClassesType\nLecture",
                LightMpeixPalette.classesTypeLecture,
                LightMpeixPalette.contentAccent,
            )
            colorItem(
                "ClassesType\nPractice",
                LightMpeixPalette.classesTypePractice,
                LightMpeixPalette.content,
            )
            colorItem(
                "ClassesType\nLab",
                LightMpeixPalette.classesTypeLab,
                LightMpeixPalette.contentAccent,
            )
        }
    }
}

internal class MpeixPaletteParameterProvider : PreviewParameterProvider<MpeixPalette> {

    override val values: Sequence<MpeixPalette>
        get() = sequenceOf(LightMpeixPalette, DarkMpeixPalette)
}
