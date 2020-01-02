package kekmech.ru.domain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kekmech.ru.core.repositories.UserRepository
import kekmech.ru.core.usecases.SetDarkThemeEnabledUseCase

class SetDarkThemeEnabledUseCaseImpl(
    private val userRepository: UserRepository
) : SetDarkThemeEnabledUseCase {
    override fun invoke(isEnabled: Boolean, recreatingActivity: AppCompatActivity) {
        if (userRepository.isDarkThemeEnabled != isEnabled) {
            userRepository.isDarkThemeEnabled = isEnabled
            recreatingActivity.recreate()
        }
    }
}