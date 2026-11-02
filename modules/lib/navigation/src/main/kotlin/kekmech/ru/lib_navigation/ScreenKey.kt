package kekmech.ru.lib_navigation

import android.os.Parcelable
import androidx.navigation3.runtime.NavKey

/**
 * Ключ экрана в back stack'е [Router].
 *
 * Ключи должны быть [Parcelable], чтобы back stack пережил смерть процесса.
 * Обычно это `@Parcelize data class` или `@Parcelize data object` внутри `impl` модуля фичи.
 * Наружу ключи не отдаются: `api` модули открывают экраны через launcher'ы.
 */
public interface ScreenKey : NavKey, Parcelable
