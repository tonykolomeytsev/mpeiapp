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

internal class ImageViewFragment : Fragment(R.layout.fragment_image_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        Picasso.get()
            .load(getArgument<String>(ARG_URL))
            .noFade()
            .into(imageView)
    }

    companion object {

        private const val ARG_URL = "Arg.Url"
        private const val ARG_TRANSITION_ELEMENT = "Arg.Transition"

        fun newInstance(url: String, transitionElementName: String?) = ImageViewFragment()
            .withArguments(
                ARG_URL to url,
                ARG_TRANSITION_ELEMENT to transitionElementName
            )
    }
}