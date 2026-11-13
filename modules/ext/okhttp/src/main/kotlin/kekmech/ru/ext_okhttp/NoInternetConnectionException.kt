package kekmech.ru.ext_okhttp

import java.io.IOException

public class NoInternetConnectionException(
    cause: Throwable? = null,
) : IOException("No internet connection!", cause)
