package ru.kekmech.common_images.imagepicker.mvi

import kekmech.ru.common_mvi.BaseFeature
import kekmech.ru.common_mvi.NoOpActor

internal class ImagePickerFeatureFactory {

    fun create(imageCountLimit: Int) = BaseFeature(
        initialState = ImagePickerState(
            imageCountLimit = imageCountLimit
        ),
        reducer = ImagePickerReducer(),
        actor = NoOpActor()
    ).start()
}