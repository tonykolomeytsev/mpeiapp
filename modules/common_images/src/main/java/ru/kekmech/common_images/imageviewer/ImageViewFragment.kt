package ru.kekmech.common_images.imageviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import coil.load
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_images.R

class ImageViewFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_MaterialComponents_Dialog_FullScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        imageView.load(getArgument<String>("url")) {
            crossfade(true)
        }
    }

    companion object {

        fun newInstance(url: String) = ImageViewFragment()
            .withArguments("url" to url)
    }
}