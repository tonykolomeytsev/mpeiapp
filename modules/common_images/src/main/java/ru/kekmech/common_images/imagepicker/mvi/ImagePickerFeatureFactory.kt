package ru.kekmech.common_images.imagepicker.mvi

import kekmech.ru.common_mvi.BaseFeature

internal class ImagePickerFeatureFactory(
    private val actor: ImagePickerActor
) {

    fun create(imageCountLimit: Int, selectedImageUrls: ArrayList<String>) = BaseFeature(
        initialState = ImagePickerState(
            imageCountLimit = imageCountLimit,
            selectedImagesUrls = selectedImageUrls.toSet()
        ),
        reducer = ImagePickerReducer(),
        actor = actor
    ).start()
}