package kekmech.ru.feature_bars_impl.data.network

import android.content.Context
import android.content.SharedPreferences
import com.tencent.mmkv.MMKV
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.CodeProvider

internal data class BarsTwoFactorSession(
    val defaultProvider: CodeProvider,
    val activeProvider: CodeProvider = defaultProvider,
    val availableProviders: List<CodeProvider>,
    val codeLength: Int = 6,
    val isTotp: Boolean = activeProvider == CodeProvider.TOTP,
    val isTotpInitialized: Boolean = false,
    val lastServerMessage: String? = null,
    val providerTid: String = defaultTidFor(activeProvider),
    val providerTidMap: Map<CodeProvider, String> = emptyMap(),
    val savedAtTimestamp: Long = System.currentTimeMillis(),
) {
    fun isExpired(now: Long = System.currentTimeMillis(), ttlMs: Long = BarsTwoFactorSessionHolder.SESSION_TTL_MS): Boolean =
        now - savedAtTimestamp > ttlMs

    companion object {
        fun defaultTidFor(provider: CodeProvider): String =
            when (provider) {
                CodeProvider.TG -> "1"
                CodeProvider.VK -> "2"
                CodeProvider.MAX -> "3"
                CodeProvider.TOTP -> "5"
                CodeProvider.EMAIL -> "5"
            }
    }
}

internal object BarsTwoFactorSessionHolder {

    const val SESSION_TTL_MS = 5 * 60 * 1000L // 5 minutes

    private const val PREFS_NAME = "bars_2fa_session"
    private const val KEY_HAS_SAVED_SESSION = "key_has_saved_session"
    private const val KEY_SAVED_AT_TIMESTAMP = "key_saved_at_timestamp"
    private const val KEY_DEFAULT_PROVIDER = "key_default_provider"
    private const val KEY_ACTIVE_PROVIDER = "key_active_provider"
    private const val KEY_AVAILABLE_PROVIDERS = "key_available_providers"
    private const val KEY_CODE_LENGTH = "key_code_length"
    private const val KEY_IS_TOTP = "key_is_totp"
    private const val KEY_IS_TOTP_INITIALIZED = "key_is_totp_initialized"
    private const val KEY_PROVIDER_TID = "key_provider_tid"
    private const val KEY_LAST_SERVER_MESSAGE = "key_last_server_message"
    private const val KEY_PROVIDER_TID_MAP = "key_provider_tid_map"

    @Volatile
    private var preferences: SharedPreferences? = null

    @Volatile
    private var _currentSession: BarsTwoFactorSession? = null

    var currentSession: BarsTwoFactorSession?
        get() {
            val inMemory = _currentSession
            if (inMemory != null) {
                if (inMemory.isExpired()) {
                    reset()
                    return null
                }
                return inMemory
            }
            val restored = restoreFromPreferences()
            if (restored != null && restored.isExpired()) {
                reset()
                return null
            }
            _currentSession = restored
            return restored
        }
        set(value) {
            _currentSession = value
            if (value != null) {
                persistToPreferences(value)
            } else {
                clearPreferences()
            }
        }

    fun init(context: Context) {
        preferences = runCatching {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }.getOrNull()
        if (_currentSession == null) {
            _currentSession = restoreFromPreferences()
        }
    }

    fun init(sharedPreferences: SharedPreferences) {
        preferences = sharedPreferences
        if (_currentSession == null) {
            _currentSession = restoreFromPreferences()
        }
    }

    fun reset() {
        _currentSession = null
        clearPreferences()
    }

    fun clear() {
        reset()
    }

    fun setActiveProvider(provider: CodeProvider) {
        val session = currentSession ?: return
        val tid = session.providerTidMap[provider] ?: BarsTwoFactorSession.defaultTidFor(provider)
        currentSession = session.copy(
            activeProvider = provider,
            isTotp = provider == CodeProvider.TOTP,
            isTotpInitialized = if (provider == CodeProvider.TOTP) session.isTotpInitialized else false,
            providerTid = tid,
            savedAtTimestamp = System.currentTimeMillis(),
        )
    }

    fun markTotpInitialized() {
        val session = currentSession ?: return
        currentSession = session.copy(
            isTotpInitialized = true,
            savedAtTimestamp = System.currentTimeMillis(),
        )
    }

    private fun getStorage(): SharedPreferences? {
        preferences?.let { return it }
        val mmkv = runCatching { MMKV.mmkvWithID(PREFS_NAME) }.getOrNull()
        if (mmkv != null) {
            preferences = mmkv
            return mmkv
        }
        return null
    }

    private fun persistToPreferences(session: BarsTwoFactorSession) {
        runCatching {
            getStorage()?.edit()?.apply {
                putBoolean(KEY_HAS_SAVED_SESSION, true)
                putLong(KEY_SAVED_AT_TIMESTAMP, session.savedAtTimestamp)
                putString(KEY_DEFAULT_PROVIDER, session.defaultProvider.name)
                putString(KEY_ACTIVE_PROVIDER, session.activeProvider.name)
                putString(
                    KEY_AVAILABLE_PROVIDERS,
                    session.availableProviders.joinToString(",") { it.name },
                )
                putInt(KEY_CODE_LENGTH, session.codeLength)
                putBoolean(KEY_IS_TOTP, session.isTotp)
                putBoolean(KEY_IS_TOTP_INITIALIZED, session.isTotpInitialized)
                putString(KEY_PROVIDER_TID, session.providerTid)
                putString(KEY_LAST_SERVER_MESSAGE, session.lastServerMessage)
                putString(
                    KEY_PROVIDER_TID_MAP,
                    session.providerTidMap.entries.joinToString(";") { "${it.key.name}:${it.value}" },
                )
                apply()
            }
        }
    }

    private fun restoreFromPreferences(): BarsTwoFactorSession? {
        return runCatching {
            val storage = getStorage() ?: return null
            if (!storage.getBoolean(KEY_HAS_SAVED_SESSION, false)) return null

            val savedAt = storage.getLong(KEY_SAVED_AT_TIMESTAMP, 0L)
            val now = System.currentTimeMillis()
            if (savedAt <= 0L || now - savedAt > SESSION_TTL_MS) {
                clearPreferences()
                return null
            }

            val defaultProviderStr = storage.getString(KEY_DEFAULT_PROVIDER, null) ?: return null
            val defaultProvider = runCatching { CodeProvider.valueOf(defaultProviderStr) }.getOrNull() ?: return null

            val activeProviderStr = storage.getString(KEY_ACTIVE_PROVIDER, null)
            val activeProvider = activeProviderStr?.let {
                runCatching { CodeProvider.valueOf(it) }.getOrNull()
            } ?: defaultProvider

            val availableProvidersStr = storage.getString(KEY_AVAILABLE_PROVIDERS, null)
            val availableProviders = availableProvidersStr?.split(",")?.mapNotNull { item ->
                runCatching { CodeProvider.valueOf(item.trim()) }.getOrNull()
            }?.ifEmpty { null } ?: listOf(defaultProvider)

            val codeLength = storage.getInt(KEY_CODE_LENGTH, 6)
            val isTotp = storage.getBoolean(KEY_IS_TOTP, activeProvider == CodeProvider.TOTP)
            val isTotpInitialized = storage.getBoolean(KEY_IS_TOTP_INITIALIZED, false)
            val providerTid = storage.getString(KEY_PROVIDER_TID, null)
                ?: BarsTwoFactorSession.defaultTidFor(activeProvider)
            val lastServerMessage = storage.getString(KEY_LAST_SERVER_MESSAGE, null)

            val tidMapStr = storage.getString(KEY_PROVIDER_TID_MAP, null)
            val providerTidMap = tidMapStr?.split(";")?.mapNotNull { entry ->
                val parts = entry.split(":")
                if (parts.size == 2) {
                    val prov = runCatching { CodeProvider.valueOf(parts[0]) }.getOrNull()
                    if (prov != null) prov to parts[1] else null
                } else null
            }?.toMap().orEmpty()

            BarsTwoFactorSession(
                defaultProvider = defaultProvider,
                activeProvider = activeProvider,
                availableProviders = availableProviders,
                codeLength = codeLength,
                isTotp = isTotp,
                isTotpInitialized = isTotpInitialized,
                lastServerMessage = lastServerMessage,
                providerTid = providerTid,
                providerTidMap = providerTidMap,
                savedAtTimestamp = savedAt,
            )
        }.getOrNull()
    }

    private fun clearPreferences() {
        runCatching {
            getStorage()?.edit()?.clear()?.apply()
        }
    }
}
