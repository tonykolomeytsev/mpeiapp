package kekmech.ru.feature_dashboard_impl.presentation.eol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import kekmech.ru.feature_dashboard_impl.R

internal enum class DonationMethod {
    SBP,
    TBANK,
    BOOSTY,
}

/**
 * One way to support the project.
 *
 * @param requisite what is copied to the clipboard on tap (phone, card number or a link), may be absent
 * @param url what the "Open" button opens, may be absent
 */
@Immutable
internal data class DonationOption(
    val method: DonationMethod,
    val requisite: String?,
    val url: String?,
)

/**
 * Builds the list of payment methods from the raw values of `donation_config.xml`.
 * Methods without any value are dropped, so an unfinished configuration never shows an empty row.
 */
internal fun donationOptions(
    sbpPhone: String,
    tbankCard: String,
    tbankUrl: String,
    boostyUrl: String,
): List<DonationOption> =
    listOfNotNull(
        DonationOption(DonationMethod.SBP, requisite = sbpPhone.clean(), url = null),
        DonationOption(DonationMethod.TBANK, requisite = tbankCard.clean(), url = tbankUrl.clean()),
        // the link is also the requisite: tap copies it, the button opens it
        DonationOption(DonationMethod.BOOSTY, requisite = boostyUrl.clean(), url = boostyUrl.clean()),
    ).filter { it.requisite != null || it.url != null }

private fun String.clean(): String? = trim().takeIf { it.isNotEmpty() }

/** Reads the options from `res/values/donation_config.xml`. */
@Composable
internal fun rememberDonationOptions(): List<DonationOption> {
    val sbpPhone = stringResource(R.string.donation_sbp_phone)
    val tbankCard = stringResource(R.string.donation_tbank_card)
    val tbankUrl = stringResource(R.string.donation_tbank_url)
    val boostyUrl = stringResource(R.string.donation_boosty_url)
    return remember(sbpPhone, tbankCard, tbankUrl, boostyUrl) {
        donationOptions(sbpPhone, tbankCard, tbankUrl, boostyUrl)
    }
}
