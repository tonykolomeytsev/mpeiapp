package ru.kekmech.common_images.imagepicker.elm

import vivid.money.elmslie.core.store.ElmStore

internal class ImagePickerFeatureFactory(
    private val actor: ImagePickerActor
) {

    fun create(imageCountLimit: Int, selectedImageUrls: ArrayList<String>) = ElmStore(
        initialState = ImagePickerState(
            imageCountLimit = imageCountLimit,
            selectedImagesUrls = selectedImageUrls.toSet()
        ),
        reducer = ImagePickerReducer(),
        actor = actor
    ).start()
}