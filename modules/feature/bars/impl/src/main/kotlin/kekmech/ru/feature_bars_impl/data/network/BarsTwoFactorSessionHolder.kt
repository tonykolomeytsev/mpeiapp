package kekmech.ru.feature_bars_impl.data.network

import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.CodeProvider

internal data class BarsTwoFactorSession(
    val defaultProvider: CodeProvider,
    val activeProvider: CodeProvider = defaultProvider,
    val availableProviders: List<CodeProvider>,
    val codeLength: Int = 6,
    val isTotp: Boolean = defaultProvider == CodeProvider.TOTP,
    val isTotpInitialized: Boolean = false,
    val lastServerMessage: String? = null,
)

internal object BarsTwoFactorSessionHolder {
    @Volatile
    var currentSession: BarsTwoFactorSession? = null

    fun reset() {
        currentSession = null
    }

    fun setActiveProvider(provider: CodeProvider) {
        val session = currentSession ?: return
        currentSession = session.copy(
            activeProvider = provider,
            isTotp = provider == CodeProvider.TOTP,
        )
    }

    fun markTotpInitialized() {
        val session = currentSession ?: return
        currentSession = session.copy(isTotpInitialized = true)
    }
}
