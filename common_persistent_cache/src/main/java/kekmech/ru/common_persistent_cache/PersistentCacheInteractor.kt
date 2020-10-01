package kekmech.ru.common_persistent_cache

import java.io.File

open class PersistentCacheInteractor {

    fun writeBytes(file: File, byteArray: ByteArray) {
        file.writeBytes(byteArray)
    }

    fun readBytes(file: File): ByteArray {
        return file.readBytes()
    }
}