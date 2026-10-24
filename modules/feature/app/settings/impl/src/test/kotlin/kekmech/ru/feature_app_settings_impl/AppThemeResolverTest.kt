package kekmech.ru.feature_app_settings_impl

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_app_settings_api.domain.model.AppSettings
import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kekmech.ru.feature_app_settings_api.domain.model.isDarkTheme

class AppThemeResolverTest : BehaviorSpec({

    fun settings(theme: AppTheme) = AppSettings(
        isDarkThemeEnabled = theme == AppTheme.Dark,
        appTheme = theme,
        isSnowEnabled = false,
        languageCode = "ru_RU",
        showNavigationButton = true,
        autoHideBottomSheet = true,
        mapAppearanceType = "scheme",
    )

    Given("Light theme") {
        Then("it is never dark") {
            settings(AppTheme.Light).isDarkTheme(isSystemInDarkTheme = true) shouldBe false
            settings(AppTheme.Light).isDarkTheme(isSystemInDarkTheme = false) shouldBe false
        }
    }

    Given("Dark theme") {
        Then("it is always dark") {
            settings(AppTheme.Dark).isDarkTheme(isSystemInDarkTheme = true) shouldBe true
            settings(AppTheme.Dark).isDarkTheme(isSystemInDarkTheme = false) shouldBe true
        }
    }

    Given("System theme") {
        Then("it follows the system night mode") {
            settings(AppTheme.System).isDarkTheme(isSystemInDarkTheme = true) shouldBe true
            settings(AppTheme.System).isDarkTheme(isSystemInDarkTheme = false) shouldBe false
        }
    }
})
