package kekmech.ru.mock_server

interface MockServerLogger {

    /**
     * Log with INFO verbosity
     */
    fun i(msg: Any)

    /**
     * Log with ERROR verbosity
     */
    fun e(msg: Any)

    /**
     * Log with DEBUG verbosity
     */
    fun d(msg: Any)
}