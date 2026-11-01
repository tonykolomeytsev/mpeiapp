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
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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

// region: light colors
private val primaryLight = Color(0xFF587FC7)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFF8AB3FF)
private val onPrimaryContainerLight = Color(0xFFFEFCFF)
private val secondaryLight = Color(0xFFF6F6F6)
private val onSecondaryLight = Color(0xFF587FC7)
private val secondaryContainerLight = Color(0xFFCFDDFF)
private val onSecondaryContainerLight = Color(0xFF53617E)
private val tertiaryLight = Color(0xFF824586)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFF9D5DA0)
private val onTertiaryContainerLight = Color(0xFFFFFBFF)
private val errorLight = Color(0xFFBA1A1A)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFFFDAD6)
private val onErrorContainerLight = Color(0xFF93000A)
private val backgroundLight = Color(0xFFFFFFFF)
private val onBackgroundLight = Color(0xFF1A1B20)
private val surfaceLight = Color(0xFFFFFFFF)
private val onSurfaceLight = Color(0xFF1A1B20)
private val surfaceVariantLight = Color(0xFFDFE2EE)
private val onSurfaceVariantLight = Color(0xFF565962)
private val outlineLight = Color(0xFF737782)
private val outlineVariantLight = Color(0xFFC3C6D2)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF2F3035)
private val inverseOnSurfaceLight = Color(0xFFF0F0F7)
private val inversePrimaryLight = Color(0xFFACC7FF)
private val surfaceDimLight = Color(0xFFDAD9E0)
private val surfaceBrightLight = Color(0xFFFFFFFF)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFF3F3FA)
private val surfaceContainerLight = Color(0xFFEEEDF4)
private val surfaceContainerHighLight = Color(0xFFE8E7EE)
private val surfaceContainerHighestLight = Color(0xFFE2E2E8)
private val markGreenLight = Color(0xFFC8D9B2)
private val onMarkGreenLight = Color(0xFF47720C)
private val markYellowLight = Color(0xFFE6D3B1)
private val onMarkYellowLight = Color(0xFFA7761C)
private val markRedLight = Color(0xFFEBBDBD)
private val onMarkRedLight = Color(0xFFB73F3F)
// end region: light colors

// region: dark colors
private val primaryDark = Color(0xFF587FC7)
private val onPrimaryDark = Color(0xFFFFFFFF)
private val primaryContainerDark = Color(0xFF6A90DA)
private val onPrimaryContainerDark = Color(0xFF001536)
private val secondaryDark = Color(0xFF2A2A2A)
private val onSecondaryDark = Color(0xFFF6F6F6)
private val secondaryContainerDark = Color(0xFF3C4965)
private val onSecondaryContainerDark = Color(0xFFABB8D9)
private val tertiaryDark = Color(0xFFF7AEF7)
private val onTertiaryDark = Color(0xFF501757)
private val tertiaryContainerDark = Color(0xFFBC79BE)
private val onTertiaryContainerDark = Color(0xFF2D0034)
private val errorDark = Color(0xFFFFB4AB)
private val onErrorDark = Color(0xFF690005)
private val errorContainerDark = Color(0xFF93000A)
private val onErrorContainerDark = Color(0xFFFFDAD6)
private val backgroundDark = Color(0xFF1A1A1A)
private val onBackgroundDark = Color(0xFFE2E2E8)
private val surfaceDark = Color(0xFF1A1A1A)
private val onSurfaceDark = Color(0xFFE2E2E8)
private val surfaceVariantDark = Color(0xFF434751)
private val onSurfaceVariantDark = Color(0xFFB0B2BD)
private val outlineDark = Color(0xFF8D909C)
private val outlineVariantDark = Color(0xFF434751)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFE2E2E8)
private val inverseOnSurfaceDark = Color(0xFF2F3035)
private val inversePrimaryDark = Color(0xFF345DA3)
private val surfaceDimDark = Color(0xFF121318)
private val surfaceBrightDark = Color(0xFF37393E)
private val surfaceContainerLowestDark = Color(0xFF0C0E13)
private val surfaceContainerLowDark = Color(0xFF1A1B20)
private val surfaceContainerDark = Color(0xFF1E2024)
private val surfaceContainerHighDark = Color(0xFF282A2F)
private val surfaceContainerHighestDark = Color(0xFF33353A)
private val markGreenDark = Color(0xFF47720C)
private val onMarkGreenDark = Color(0xCCFFFFFF)
private val markYellowDark = Color(0xFFA7761C)
private val onMarkYellowDark = Color(0xCCFFFFFF)
private val markRedDark = Color(0xFFB73F3F)
private val onMarkRedDark = Color(0xCCFFFFFF)
// end region: dark colors

@Suppress("LongParameterList")
@Immutable
public class MpeixPalette internal constructor(
    public val primary: Color,
    public val onPrimary: Color,
    public val secondary: Color,
    public val onSecondary: Color,
    public val surface: Color,
    public val surfaceContainer: Color,
    public val surfaceContainerHighest: Color,
    public val onSurface: Color,
    public val onSurfaceVariant: Color,
    public val outline: Color,
    public val outlineVariant: Color,
    public val error: Color,
    public val onError: Color,
    // Backgrounds and texts of good, satisfactory and bad marks in BARS
    public val markGreen: Color,
    public val onMarkGreen: Color,
    public val markYellow: Color,
    public val onMarkYellow: Color,
    public val markRed: Color,
    public val onMarkRed: Color,
)

public val LightMpeixPalette: MpeixPalette = MpeixPalette(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    surface = surfaceLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
    onSurface = onSurfaceLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    error = errorLight,
    onError = onErrorLight,
    markGreen = markGreenLight,
    onMarkGreen = onMarkGreenLight,
    markYellow = markYellowLight,
    onMarkYellow = onMarkYellowLight,
    markRed = markRedLight,
    onMarkRed = onMarkRedLight,
)

public val DarkMpeixPalette: MpeixPalette = MpeixPalette(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    surface = surfaceDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
    onSurface = onSurfaceDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    error = errorDark,
    onError = onErrorDark,
    markGreen = markGreenDark,
    onMarkGreen = onMarkGreenDark,
    markYellow = markYellowDark,
    onMarkYellow = onMarkYellowDark,
    markRed = markRedDark,
    onMarkRed = onMarkRedDark,
)

internal val LightMpeixScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

internal val DarkMpeixScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
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
            color = palette.surface,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(Modifier.padding(bottom = 12.dp)) {
            colorItem("Primary", palette.primary, palette.onPrimary)
            colorItem("OnPrimary", palette.onPrimary, palette.primary)
            colorItem("Surface", palette.surface, palette.onSurface)
            colorItem("Surface\nContainer", palette.surfaceContainer, palette.onSurface)
            colorItem(
                "Surface\nContainer\nHighest",
                palette.surfaceContainerHighest,
                palette.onSurface
            )
        }
        Row(Modifier.padding(bottom = 12.dp)) {
            colorItem("Secondary", palette.secondary, palette.onSecondary)
            colorItem("OnSecondary", palette.onSecondary, palette.secondary)
            colorItem("OnSurface", palette.onSurface, palette.surface)
            colorItem("OnSurface\nVariant", palette.onSurfaceVariant, palette.surface)
            colorItem("Error", palette.error, palette.onError)
        }
        Row(Modifier.padding(bottom = 12.dp)) {
            colorItem("Outline", palette.outline, palette.onSurface)
            colorItem("Outline\nVariant", palette.outlineVariant, palette.onSurface)
            colorItem("Mark\nGreen", palette.markGreen, palette.onMarkGreen)
            colorItem("Mark\nYellow", palette.markYellow, palette.onMarkYellow)
            colorItem("Mark\nRed", palette.markRed, palette.onMarkRed)
        }
    }
}

internal class MpeixPaletteParameterProvider : PreviewParameterProvider<MpeixPalette> {

    override val values: Sequence<MpeixPalette>
        get() = sequenceOf(LightMpeixPalette, DarkMpeixPalette)
}
