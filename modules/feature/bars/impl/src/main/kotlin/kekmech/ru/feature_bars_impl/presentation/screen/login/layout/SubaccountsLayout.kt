package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.tonykolomeytsev.mpeix.sdk.LoginState
import io.github.tonykolomeytsev.mpeix.sdk.LoginStep
import io.github.tonykolomeytsev.mpeix.sdk.Subaccount
import io.github.tonykolomeytsev.mpeix.sdk.SubaccountId
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginActions
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBarNavigationIconScope.CloseIconButton
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val DISABLED_ALPHA = 0.38f

@Composable
internal fun SubaccountsLayout(
    step: LoginStep.ChooseSubaccount,
    login: LoginState,
    actions: BarsLoginActions,
) {
    // The state doesn't say which subaccount is being opened, so the tapped one is kept here
    var chosenId by rememberSaveable { mutableStateOf<String?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { BackIconButton(actions::onBack) },
                actions = { CloseIconButton(actions::onClose) },
            )
        },
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .adaptiveContentWidth()
                .padding(top = 8.dp, bottom = 16.dp),
        ) {
            Text(
                text = "Выберите профиль",
                style = MpeixTheme.typography.headlineBig,
                color = MpeixTheme.palette.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Text(
                text = "У вашего аккаунта несколько ступеней обучения. Профиль можно будет сменить позже",
                style = MpeixTheme.typography.bodyLarge,
                color = MpeixTheme.palette.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            step.subaccounts.forEach { subaccount ->
                val isChosen = login.isWorking && subaccount.id.value == chosenId
                ListItem(
                    headlineText = subaccount.title,
                    supportingText = listOfNotNull(subaccount.educationLevel, subaccount.group)
                        .joinToString(", ")
                        .takeIf {
                            it.isNotEmpty() && !it.equals(
                                subaccount.title,
                                ignoreCase = true
                            )
                        },
                    trailingContent = if (isChosen) {
                        {
                            CircularProgressIndicator(
                                color = MpeixTheme.palette.primary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    } else {
                        null
                    },
                    modifier = Modifier
                        .alpha(if (login.isWorking && !isChosen) DISABLED_ALPHA else 1f)
                        .clickable(enabled = !login.isWorking) {
                            chosenId = subaccount.id.value
                            actions.onChooseSubaccount(subaccount.id)
                        },
                )
            }
            login.error?.let { error ->
                Text(
                    text = error.text(),
                    style = MpeixTheme.typography.bodyMedium,
                    color = MpeixTheme.palette.error,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun SubaccountsPreview() {
    val step = LoginStep.ChooseSubaccount(
        listOf(
            Subaccount(SubaccountId("1"), "Бакалавриат, А-08-19", "А-08-19", "Бакалавриат"),
            Subaccount(SubaccountId("2"), "Магистратура, А-08м-23", "А-08м-23", "Магистратура"),
        )
    )
    SubaccountsLayout(step = step, login = previewLoginState(step), actions = NoOpActions)
}
