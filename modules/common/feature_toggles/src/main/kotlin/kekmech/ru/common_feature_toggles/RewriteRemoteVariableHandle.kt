package kekmech.ru.common_feature_toggles

interface RewriteRemoteVariableHandle {

    /**
     * Override variable value for debug purposes
     */
    fun override(name: String, value: String?)

    /**
     * Check is value rewritten from debug menu
     */
    fun isRewritten(name: String): Boolean
}
