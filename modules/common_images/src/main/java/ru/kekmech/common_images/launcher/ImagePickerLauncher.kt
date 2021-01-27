package ru.kekmech.common_images.launcher

import androidx.fragment.app.Fragment

interface ImagePickerLauncher {

    fun launch(
        requestCode: Int,
        targetFragment: Fragment,
        imageCountLimit: Int = DEFAULT_IMAGE_COUNT_LIMIT
    )

    companion object {
        const val DEFAULT_IMAGE_COUNT_LIMIT = 10
    }
}