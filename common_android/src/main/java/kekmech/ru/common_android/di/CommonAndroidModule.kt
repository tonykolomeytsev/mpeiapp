package kekmech.ru.common_android.di

import kekmech.ru.common_android.clipboard.ClipboardRepository
import kekmech.ru.common_di.ModuleProvider

object CommonAndroidModule : ModuleProvider({
    factory { ClipboardRepository(get()) }
})