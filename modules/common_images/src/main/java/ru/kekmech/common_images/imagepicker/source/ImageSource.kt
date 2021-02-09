package ru.kekmech.common_images.imagepicker.source

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import io.reactivex.Single

internal class ImageSource(
    private val contentResolver: ContentResolver
) {

    fun getImagesUrls(
        page: Int,
        albumId: String? = null
    ): Single<List<String>> = Single.create { emitter ->
        val offset = page * DEFAULT_PAGE_SIZE
        var photoCursor: Cursor? = null
        val urls = mutableListOf<String>()
        try {
            if (albumId == null) {
                photoCursor = contentResolver.query(
                    CURSOR_URI,
                    arrayOf(
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DATA
                    ),
                    null,
                    null,
                    "$ORDER_BY LIMIT $DEFAULT_PAGE_SIZE OFFSET $offset"
                )
            } else {
                photoCursor = contentResolver.query(
                    CURSOR_URI,
                    arrayOf(
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DATA
                    ),
                    "${MediaStore.Images.ImageColumns.BUCKET_ID} =?",
                    arrayOf(albumId),
                    "$ORDER_BY LIMIT $DEFAULT_PAGE_SIZE OFFSET $offset"
                )
            }
            if (photoCursor?.isAfterLast != null) {
                photoCursor.doWhile {
                    urls += "file:///" + photoCursor.getString((photoCursor.getColumnIndex(MediaStore.Images.Media.DATA)))
                }
            }
        } catch (e: Exception) {
            emitter.onError(e)
        } finally {
            if (photoCursor != null && !photoCursor.isClosed) {
                photoCursor.close()
            }
        }
        emitter.onSuccess(urls)
    }

    private fun Cursor.doWhile(action: () -> Unit) {
        this.use {
            if (this.moveToFirst()) {
                do {
                    action()
                } while (this.moveToNext())
            }
        }
    }

    companion object {
        private val CURSOR_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        private const val ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC"
        private const val DEFAULT_PAGE_SIZE = 20
    }
}