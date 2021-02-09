package ru.kekmech.common_images.imagepicker

import kekmech.ru.coreui.items.PullItem
import ru.kekmech.common_images.imagepicker.adapter.ImageItem
import ru.kekmech.common_images.imagepicker.mvi.ImagePickerState

internal object ImagePickerListConverter {

    fun map(state: ImagePickerState): List<Any> = mutableListOf<Any>().apply {
        add(PullItem)

        addAll(state.imagesUrls.map { url ->
            val selectedIndex = state.selectedImagesUrls.indexOf(url)
            ImageItem(url, selectedIndex != -1, selectedIndex + 1)
        })
    }
}