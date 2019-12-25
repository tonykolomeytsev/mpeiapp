package kekmech.ru.repository

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso
import kekmech.ru.core.dto.FeedCarousel
import kekmech.ru.core.gateways.PicassoFirebaseInstance
import kekmech.ru.core.repositories.FeedRepository
import kekmech.ru.repository.utils.FirebaseRequestHandler

class FeedRepositoryImpl(
    context: Context
) : FeedRepository {
    override val feedCarousel: MutableLiveData<FeedCarousel> = MutableLiveData()
    override val picassoInstance: PicassoFirebaseInstance = Picasso.Builder(context)
        .addRequestHandler(FirebaseRequestHandler())
        .build()
        .toPicassoFirebaseInstance()

    private val carouselRef = FirebaseFirestore.getInstance()
        .collection("carousel")

    override fun loadFreshCarousel() {
        carouselRef.get()
            .addOnSuccessListener(::onCarouselComplete)
            .addOnFailureListener { Log.d("FeedRepository", it.toString()) }
    }

    private fun onCarouselComplete(snapshot: QuerySnapshot) {
        val items = mutableListOf<FeedCarousel.Item>()
        snapshot.documents.forEach {
            val item = it.toObject(FeedCarousel.Item::class.java)
            if (item != null) {
                items += item
                FirebaseFirestore.getInstance()
            }
        }
        feedCarousel.value = FeedCarousel(items)
    }


    private fun Picasso.toPicassoFirebaseInstance() = object : PicassoFirebaseInstance {
        override fun load(link: String, imageView: ImageView) {
            if (link.isNotEmpty())
                this@toPicassoFirebaseInstance.load(link).into(imageView)
        }
    }
}
