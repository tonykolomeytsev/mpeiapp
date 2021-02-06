package ru.kekmech.common_images.launcher

import androidx.fragment.app.Fragment

interface ImagePickerLauncher {

    fun launch(
        requestCode: Int,
        targetFragment: Fragment,
        imageCountLimit: Int = DEFAULT_IMAGE_COUNT_LIMIT,
        alreadySelectedImages: List<String> = emptyList()
    )

    companion object {
        const val DEFAULT_IMAGE_COUNT_LIMIT = 10
        const val EXTRA_SELECTED_IMAGES = "SelectedImages"
    }
}