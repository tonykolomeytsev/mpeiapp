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
}

data class MpeixPalette(
    val Primary: Color,
    val Secondary: Color,
    val Tertiary: Color,
    val Background: Color,
    val Surface: Color,
    val SurfacePlus1: Color,
    val SurfacePlus2: Color,
    val SurfacePlus3: Color,
    val PrimaryContainer: Color,
    val SecondaryContainer: Color,
    val Content: Color,
    val ContentAccent: Color,
    val ContentVariant: Color,
    val ContentDisabled: Color,
    val Outline: Color,
    val ClassesTypeLecture: Color = Color(0xFF16B37C),
    val ClassesTypePractice: Color = Color(0xFFE8BC4C),
    val ClassesTypeLab: Color = Color(0xFFE864AB),
)

val LightMpeixPalette = MpeixPalette(
    Primary = MpeixColors.Primary60,
    Secondary = MpeixColors.Secondary60,
    Tertiary = MpeixColors.Tertiary50,
    Background = MpeixColors.Neutral97,
    Surface = MpeixColors.Neutral100,
    SurfacePlus1 = MpeixColors.Neutral98,
    SurfacePlus2 = MpeixColors.Neutral95,
    SurfacePlus3 = MpeixColors.Neutral93,
    PrimaryContainer = MpeixColors.Primary90,
    SecondaryContainer = MpeixColors.Secondary90,
    Content = MpeixColors.Neutral20,
    ContentAccent = MpeixColors.Neutral100,
    ContentVariant = MpeixColors.Neutral40,
    ContentDisabled = MpeixColors.Neutral60,
    Outline = MpeixColors.Neutral80,
)

val DarkMpeixPalette = MpeixPalette(
    Primary = MpeixColors.Primary60,
    Secondary = MpeixColors.Secondary60,
    Tertiary = MpeixColors.Tertiary50,
    Background = MpeixColors.Neutral10,
    Surface = MpeixColors.Neutral20,
    SurfacePlus1 = MpeixColors.Neutral25,
    SurfacePlus2 = MpeixColors.Neutral30,
    SurfacePlus3 = MpeixColors.Neutral35,
    PrimaryContainer = MpeixColors.Primary30,
    SecondaryContainer = MpeixColors.Secondary30,
    Content = MpeixColors.Neutral90,
    ContentAccent = MpeixColors.Neutral100,
    ContentVariant = MpeixColors.Neutral80,
    ContentDisabled = MpeixColors.Neutral70,
    Outline = MpeixColors.Neutral60,
)

@Composable
@Preview(device = "spec:width=1920px,height=1080px,dpi=240")
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
                color = palette.Surface,
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
            color = palette.Content,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(Modifier.padding(bottom = 12.dp)) {
            colorItem(
                name = "Background",
                backgroundColor = palette.Background,
                textColor = palette.Content,
            )
            colorItem(
                name = "Surface",
                backgroundColor = palette.Surface,
                textColor = palette.Content,
            )
            colorItem(
                name = "Surface + 1",
                backgroundColor = palette.SurfacePlus1,
                textColor = palette.Content,
            )
            colorItem(
                name = "Surface + 2",
                backgroundColor = palette.SurfacePlus2,
                textColor = palette.Content,
            )
            colorItem(
                name = "Surface + 3",
                backgroundColor = palette.SurfacePlus3,
                textColor = palette.Content,
            )
        }
        Row(Modifier.padding(bottom = 12.dp)) {
            colorItem(
                name = "Primary",
                backgroundColor = palette.Primary,
                textColor = palette.ContentAccent,
            )
            colorItem(
                name = "Secondary",
                backgroundColor = palette.Secondary,
                textColor = palette.ContentAccent,
            )
            colorItem(
                name = "Tertiary",
                backgroundColor = palette.Tertiary,
                textColor = palette.ContentAccent,
            )
            colorItem(
                name = "ContentAccent",
                backgroundColor = palette.ContentAccent,
                textColor = MpeixColors.Neutral10,
            )
        }
        Row(Modifier.padding(bottom = 12.dp)) {
            colorItem(
                name = "Primary\nContainer",
                backgroundColor = palette.PrimaryContainer,
                textColor = palette.Content,
            )
            colorItem(
                name = "Secondary\nContainer",
                backgroundColor = palette.SecondaryContainer,
                textColor = palette.Content,
            )
            colorItem(
                name = "Content",
                backgroundColor = palette.Content,
                textColor = palette.Surface,
            )
            colorItem(
                name = "ContentVariant",
                backgroundColor = palette.ContentVariant,
                textColor = palette.Surface,
            )
            colorItem(
                name = "Content\nDisabled",
                backgroundColor = palette.ContentDisabled,
                textColor = palette.ContentAccent,
            )
            colorItem(
                name = "Outline",
                backgroundColor = palette.Outline,
                textColor = palette.Content,
            )
        }
        Row {
            colorItem(
                name = "ClassesType\nLecture",
                backgroundColor = LightMpeixPalette.ClassesTypeLecture,
                textColor = LightMpeixPalette.ContentAccent,
            )
            colorItem(
                name = "ClassesType\nPractice",
                backgroundColor = LightMpeixPalette.ClassesTypePractice,
                textColor = LightMpeixPalette.Content,
            )
            colorItem(
                name = "ClassesType\nLab",
                backgroundColor = LightMpeixPalette.ClassesTypeLab,
                textColor = LightMpeixPalette.ContentAccent,
            )
        }
    }
}

internal class MpeixPaletteParameterProvider : PreviewParameterProvider<MpeixPalette> {

    override val values: Sequence<MpeixPalette>
        get() = sequenceOf(LightMpeixPalette, DarkMpeixPalette)
}
