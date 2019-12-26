package kekmech.ru.repository.utils

import android.graphics.BitmapFactory
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StreamDownloadTask
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.ExecutionException
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestHandler


class FirebaseRequestHandler : RequestHandler() {
    override fun canHandleRequest(data: com.squareup.picasso.Request): Boolean {
        val scheme: String = data.uri.scheme ?: ""
        return SCHEME_FIREBASE_STORAGE == scheme
    }

    @Throws(IOException::class)
    override fun load(
        request: com.squareup.picasso.Request,
        networkPolicy: Int
    ): Result? {
        val gsReference =
            FirebaseStorage.getInstance().getReferenceFromUrl(request.uri.toString())
        val mStreamTask: StreamDownloadTask
        val inputStream: InputStream
        mStreamTask = gsReference.stream
        return try {
            inputStream =
                Tasks.await(mStreamTask)
                    .stream
            Log.i("FireBaseRequestHandler", "Loaded " + gsReference.path)
            Result(
                BitmapFactory.decodeStream(
                    inputStream
                ), Picasso.LoadedFrom.NETWORK
            )
        } catch (e: ExecutionException) {
            throw IOException()
        } catch (e: InterruptedException) {
            throw IOException()
        }
    }

    companion object {
        private const val SCHEME_FIREBASE_STORAGE = "gs"
    }
}