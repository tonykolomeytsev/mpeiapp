package kekmech.ru.ui_kit_topappbar.topappbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
public fun TopAppBar(
    navigationIcon: @Composable TopAppBarNavigationIconScope.() -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    palette: TopAppBarPalette = TopAppBarPalette.Default,
    form: TopAppBarForm = TopAppBarForm.Small,
) {
    androidx.compose.material3.TopAppBar(
        title = {
            title?.let { text ->
                Text(
                    text = text,
                    style = form.textStyle,
                )
            }
        },
        navigationIcon = { navigationIcon.invoke(TopAppBarNavigationIconScope) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = palette.background,
            scrolledContainerColor = palette.background,
            navigationIconContentColor = palette.title,
            titleContentColor = palette.title,
            actionIconContentColor = palette.title,
        ),
    )
}

public object TopAppBarNavigationIconScope {

    @Composable
    public fun BackIconButton(onClick: () -> Unit) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = null,
            )
        }
    }

    @Composable
    public fun CloseIconButton(onClick: () -> Unit) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
            )
        }
    }
}

@Composable
@Preview
private fun TopAppBarPreview() {
    MpeixTheme {
        TopAppBar(
            navigationIcon = {
                BackIconButton(
                    onClick = { /* no-op */ },
                )
            },
            modifier = Modifier,
            title = "Hello",
        )
    }
}