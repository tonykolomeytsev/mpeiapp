package kekmech.ru.feature_dashboard_impl.presentation.eol

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ext_android.copyToClipboard
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.feature_dashboard_impl.R
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.launch

@Composable
internal fun EolScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    donationOptions: List<DonationOption> = rememberDonationOptions(),
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(Strings.about_project_title),
                navigationIcon = { BackIconButton(onBackClick) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentColor = MpeixTheme.palette.content,
        containerColor = MpeixTheme.palette.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MpeixTheme.palette.primaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(Strings.about_project_subtitle),
                        style = MpeixTheme.typography.header3.copy(
                            fontWeight = FontWeight.Bold,
                            color = MpeixTheme.palette.content,
                        ),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(Strings.about_project_intro),
                        style = MpeixTheme.typography.paragraphNormal.copy(
                            color = MpeixTheme.palette.content,
                            lineHeight = 20.sp,
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(Strings.about_features_title),
                style = MpeixTheme.typography.header4.copy(
                    fontWeight = FontWeight.Bold,
                    color = MpeixTheme.palette.content,
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))

            AboutFeatureItem(
                title = stringResource(Strings.about_feature_parser_title),
                desc = stringResource(Strings.about_feature_parser_desc),
            )
            AboutFeatureItem(
                title = stringResource(Strings.about_feature_maps_title),
                desc = stringResource(Strings.about_feature_maps_desc),
            )
            AboutFeatureItem(
                title = stringResource(Strings.about_feature_ui_title),
                desc = stringResource(Strings.about_feature_ui_desc),
            )
            AboutFeatureItem(
                title = stringResource(Strings.about_feature_opensource_title),
                desc = stringResource(Strings.about_feature_opensource_desc),
            )

            if (donationOptions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                SupportCard(
                    options = donationOptions,
                    onCopy = { label, text ->
                        val copied = context.copyToClipboard(text = text, label = label)
                        val message = context.getString(
                            if (copied) Strings.support_copied else Strings.support_copy_failed,
                            label,
                        )
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(message)
                        }
                    },
                    onOpen = { url -> context.openLinkExternal(url) },
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(R.drawable.ic_banner_open_source),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AboutFeatureItem(title: String, desc: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MpeixTheme.palette.surfacePlus1,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MpeixTheme.typography.paragraphNormal.copy(
                    fontWeight = FontWeight.Bold,
                    color = MpeixTheme.palette.content,
                ),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                style = MpeixTheme.typography.paragraphNormal.copy(
                    color = MpeixTheme.palette.contentVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun EolScreenPreview() {
    MpeixTheme {
        EolScreen(
            onBackClick = { /* no-op */ },
            donationOptions = donationOptions(
                sbpPhone = "+7 900 000-00-00",
                tbankCard = "0000 0000 0000 0000",
                tbankUrl = "https://www.tbank.ru/cf/example",
                boostyUrl = "https://boosty.to/example",
            ),
        )
    }
}