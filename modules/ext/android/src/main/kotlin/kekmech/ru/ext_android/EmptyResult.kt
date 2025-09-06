package kekmech.ru.ext_android

import java.io.Serializable

public data object EmptyResult : Serializable {
    private fun readResolve(): Any = EmptyResult
}
