package ru.kekmech.common_images.imagepicker.mvi

import kekmech.ru.common_mvi.BaseFeature

internal class ImagePickerFeatureFactory(
    private val actor: ImagePickerActor
) {

    fun create(imageCountLimit: Int) = BaseFeature(
        initialState = ImagePickerState(
            imageCountLimit = imageCountLimit
        ),
        reducer = ImagePickerReducer(),
        actor = actor
    ).start()
}