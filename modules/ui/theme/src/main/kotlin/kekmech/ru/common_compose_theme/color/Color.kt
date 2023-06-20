package kekmech.ru.common_compose_theme.color

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
import kekmech.ru.common_compose_theme.typography.RobotoFontFamily

@Immutable
object MpeixColors {

    val Primary90 = Color(0xFFDBE6FA)
    val Primary60 = Color(0xFF4C83E8)
    val Primary30 = Color(0xFF113883)
    val Secondary90 = Color(0xFFD1EDFA)
    val Secondary60 = Color(0xFF4BB7E9)
    val Secondary30 = Color(0xFF105F84)
    val Tertiary50 = Color(0xFF1CE39D)
    val Neutral100 = Color(0xFFFFFFFF)
    val Neutral98 = Color(0xFFF6F7F9)
    val Neutral97 = Color(0xFFF3F5F7)
    val Neutral95 = Color(0xFFF0F2F5)
    val Neutral93 = Color(0xFFE3E7ED)
    val Neutral90 = Color(0xFFE0E4EB)
    val Neutral80 = Color(0xFFC2CAD6)
    val Neutral70 = Color(0xFFA3AFC2)
    val Neutral60 = Color(0xFF8494AE)
    val Neutral40 = Color(0xFF51617B)
    val Neutral35 = Color(0xFF3B4759)
    val Neutral30 = Color(0xFF333D4D)
    val Neutral25 = Color(0xFF2A3340)
    val Neutral20 = Color(0xFF232A35)
    val Neutral10 = Color(0xFF14181F)
    val ClassesTypeLecture = Color(0xFF16B37C)
    val ClassesTypePractice = Color(0xFFE8BC4C)
    val ClassesTypeLab = Color(0xFFE864AB)
}

@Suppress("LongParameterList")
@Immutable
class MpeixPalette internal constructor(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val background: Color,
    val surface: Color,
    val surfacePlus1: Color,
    val surfacePlus2: Color,
    val surfacePlus3: Color,
    val primaryContainer: Color,
    val secondaryContainer: Color,
    val content: Color,
    val contentAccent: Color,
    val contentVariant: Color,
    val contentDisabled: Color,
    val outline: Color,
    val classesTypeLecture: Color = MpeixColors.ClassesTypeLecture,
    val classesTypePractice: Color = MpeixColors.ClassesTypePractice,
    val classesTypeLab: Color = MpeixColors.ClassesTypeLab,
)

val LightMpeixPalette = MpeixPalette(
    primary = MpeixColors.Primary60,
    secondary = MpeixColors.Secondary60,
    tertiary = MpeixColors.Tertiary50,
    background = MpeixColors.Neutral97,
    surface = MpeixColors.Neutral100,
    surfacePlus1 = MpeixColors.Neutral98,
    surfacePlus2 = MpeixColors.Neutral95,
    surfacePlus3 = MpeixColors.Neutral93,
    primaryContainer = MpeixColors.Primary90,
    secondaryContainer = MpeixColors.Secondary90,
    content = MpeixColors.Neutral20,
    contentAccent = MpeixColors.Neutral100,
    contentVariant = MpeixColors.Neutral40,
    contentDisabled = MpeixColors.Neutral60,
    outline = MpeixColors.Neutral80,
)

val DarkMpeixPalette = MpeixPalette(
    primary = MpeixColors.Primary60,
    secondary = MpeixColors.Secondary60,
    tertiary = MpeixColors.Tertiary50,
    background = MpeixColors.Neutral10,
    surface = MpeixColors.Neutral20,
    surfacePlus1 = MpeixColors.Neutral25,
    surfacePlus2 = MpeixColors.Neutral30,
    surfacePlus3 = MpeixColors.Neutral35,
    primaryContainer = MpeixColors.Primary30,
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
            colorItem(
                name = "Background",
                backgroundColor = palette.background,
                textColor = palette.content,
            )
            colorItem(
                name = "Surface",
                backgroundColor = palette.surface,
                textColor = palette.content,
            )
            colorItem(
                name = "Surface + 1",
                backgroundColor = palette.surfacePlus1,
                textColor = palette.content,
            )
            colorItem(
                name = "Surface + 2",
                backgroundColor = palette.surfacePlus2,
                textColor = palette.content,
            )
            colorItem(
                name = "Surface + 3",
                backgroundColor = palette.surfacePlus3,
                textColor = palette.content,
            )
        }
        Row(Modifier.padding(bottom = 12.dp)) {
            colorItem(
                name = "Primary",
                backgroundColor = palette.primary,
                textColor = palette.contentAccent,
            )
            colorItem(
                name = "Secondary",
                backgroundColor = palette.secondary,
                textColor = palette.contentAccent,
            )
            colorItem(
                name = "Tertiary",
                backgroundColor = palette.tertiary,
                textColor = palette.contentAccent,
            )
            colorItem(
                name = "ContentAccent",
                backgroundColor = palette.contentAccent,
                textColor = MpeixColors.Neutral10,
            )
        }
        Row(Modifier.padding(bottom = 12.dp)) {
            colorItem(
                name = "Primary\nContainer",
                backgroundColor = palette.primaryContainer,
                textColor = palette.content,
            )
            colorItem(
                name = "Secondary\nContainer",
                backgroundColor = palette.secondaryContainer,
                textColor = palette.content,
            )
            colorItem(
                name = "Content",
                backgroundColor = palette.content,
                textColor = palette.surface,
            )
            colorItem(
                name = "ContentVariant",
                backgroundColor = palette.contentVariant,
                textColor = palette.surface,
            )
            colorItem(
                name = "Content\nDisabled",
                backgroundColor = palette.contentDisabled,
                textColor = palette.contentAccent,
            )
            colorItem(
                name = "Outline",
                backgroundColor = palette.outline,
                textColor = palette.content,
            )
        }
        Row {
            colorItem(
                name = "ClassesType\nLecture",
                backgroundColor = LightMpeixPalette.classesTypeLecture,
                textColor = LightMpeixPalette.contentAccent,
            )
            colorItem(
                name = "ClassesType\nPractice",
                backgroundColor = LightMpeixPalette.classesTypePractice,
                textColor = LightMpeixPalette.content,
            )
            colorItem(
                name = "ClassesType\nLab",
                backgroundColor = LightMpeixPalette.classesTypeLab,
                textColor = LightMpeixPalette.contentAccent,
            )
        }
    }
}

internal class MpeixPaletteParameterProvider : PreviewParameterProvider<MpeixPalette> {

    override val values: Sequence<MpeixPalette>
        get() = sequenceOf(LightMpeixPalette, DarkMpeixPalette)
}
