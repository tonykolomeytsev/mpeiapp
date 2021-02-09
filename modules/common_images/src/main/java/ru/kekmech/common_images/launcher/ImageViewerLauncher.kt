package ru.kekmech.common_images.launcher

import android.widget.ImageView

interface ImageViewerLauncher {
    fun launch(imageUrl: String, transitionImage: ImageView? = null)
}