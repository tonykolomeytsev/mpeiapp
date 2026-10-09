package kekmech.ru.feature_bars_impl.data.network

import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.CodeProvider

internal data class BarsTwoFactorSession(
    val defaultProvider: CodeProvider,
    val availableProviders: List<CodeProvider>,
    val codeLength: Int = 6,
    val isTotp: Boolean = false,
    val lastServerMessage: String? = null,
)

internal object BarsTwoFactorSessionHolder {
    @Volatile
    var currentSession: BarsTwoFactorSession? = null

    fun reset() {
        currentSession = null
    }
}
