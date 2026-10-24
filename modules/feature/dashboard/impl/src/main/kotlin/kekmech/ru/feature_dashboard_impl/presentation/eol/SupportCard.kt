package kekmech.ru.feature_dashboard_impl.presentation.eol

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * "Support the project" card. Tap on a row copies its requisite, the button opens the link.
 * All colors come from the theme, so the card follows light / dark / system themes.
 */
@Composable
internal fun SupportCard(
    options: List<DonationOption>,
    onCopy: (label: String, text: String) -> Unit,
    onOpen: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (options.isEmpty()) return
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MpeixTheme.palette.surfacePlus1),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text(
                text = stringResource(Strings.support_title),
                style = MpeixTheme.typography.header4,
                color = MpeixTheme.palette.content,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(Strings.support_description),
                style = MpeixTheme.typography.paragraphNormal,
                color = MpeixTheme.palette.contentVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(8.dp))
            options.forEach { option ->
                DonationRow(option = option, onCopy = onCopy, onOpen = onOpen)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(Strings.support_copy_hint),
                style = MpeixTheme.typography.labelNormal,
                color = MpeixTheme.palette.contentDisabled,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
private fun DonationRow(
    option: DonationOption,
    onCopy: (label: String, text: String) -> Unit,
    onOpen: (url: String) -> Unit,
) {
    val title = stringResource(option.method.titleRes())
    val requisite = option.requisite
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (requisite != null) {
                    Modifier.clickable(
                        onClickLabel = stringResource(Strings.support_copy_action),
                        onClick = { onCopy(title, requisite) },
                    )
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MpeixTheme.typography.paragraphNormalAccent.copy(fontWeight = FontWeight.Bold),
                color = MpeixTheme.palette.content,
            )
            if (requisite != null) {
                Text(
                    text = requisite,
                    style = MpeixTheme.typography.paragraphNormal,
                    color = MpeixTheme.palette.contentVariant,
                )
            }
        }
        option.url?.let { url ->
            TextButton(onClick = { onOpen(url) }) {
                Text(
                    text = stringResource(Strings.support_open),
                    color = MpeixTheme.palette.primary,
                )
            }
        }
    }
}

private fun DonationMethod.titleRes(): Int =
    when (this) {
        DonationMethod.LAVA -> Strings.support_method_lava
        DonationMethod.SBP -> Strings.support_method_sbp
        DonationMethod.TBANK -> Strings.support_method_tbank
        DonationMethod.BOOSTY -> Strings.support_method_boosty
    }

@Preview(showBackground = true)
@Composable
private fun SupportCardPreview() {
    MpeixTheme {
        SupportCard(
            options = donationOptions(
                sbpPhone = "+7 900 000-00-00",
                tbankCard = "0000 0000 0000 0000",
                tbankUrl = "https://www.tbank.ru/cf/example",
                boostyUrl = "https://boosty.to/example",
                lavaUrl = "https://app.lava.top/pharosteam",
            ),
            onCopy = { _, _ -> },
            onOpen = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
