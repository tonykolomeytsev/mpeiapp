package kekmech.ru.feature_onboarding_impl.presentation.item

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kekmech.ru.feature_onboarding_impl.R
import kekmech.ru.res_strings.R.string as Strings

internal enum class PromoPage(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val subtitle: Int
) {

    SCHEDULE(
        icon = R.drawable.ill_schedule,
        title = Strings.onboarding_welcome_title_schedule,
        subtitle = Strings.onboarding_welcome_subtitle_schedule
    ),

    MAP(
        icon = R.drawable.ill_map,
        title = Strings.onboarding_welcome_title_map,
        subtitle = Strings.onboarding_welcome_subtitle_map
    ),

    BARS(
        icon = R.drawable.ill_bars,
        title = Strings.onboarding_welcome_title_bars,
        subtitle = Strings.onboarding_welcome_subtitle_bars
    ),

    NOTES(
        icon = R.drawable.ill_notes,
        title = Strings.onboarding_welcome_title_notes,
        subtitle = Strings.onboarding_welcome_subtitle_notes
    ),
}
