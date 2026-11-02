package kekmech.ru.lib_navigation

import androidx.navigation3.runtime.EntryProviderScope

/**
 * Регистрация экранов фичи в общем `NavDisplay`.
 *
 * Каждый feature-модуль кладет свою реализацию в Koin, приложение собирает их через `getAll()`.
 */
public fun interface NavEntries {

    public fun EntryProviderScope<ScreenKey>.register()
}
