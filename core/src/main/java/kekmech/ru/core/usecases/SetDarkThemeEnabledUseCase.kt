package kekmech.ru.core.usecases

import androidx.appcompat.app.AppCompatActivity

interface SetDarkThemeEnabledUseCase {
    operator fun invoke(isEnabled: Boolean, recreatingActivity: AppCompatActivity)
}