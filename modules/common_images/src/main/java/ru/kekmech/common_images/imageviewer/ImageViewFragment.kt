package ru.kekmech.common_images.imageviewer

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_images.R

class ImageViewFragment : Fragment(R.layout.fragment_image_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        Picasso.get()
            .load(getArgument<String>("url"))
            .into(imageView)
    }

    companion object {

        fun newInstance(url: String) = ImageViewFragment()
            .withArguments("url" to url)
    }
}