package ru.kekmech.common_images.imagepicker.elm

import io.reactivex.Observable
import ru.kekmech.common_images.imagepicker.elm.ImagePickerEvent.News
import ru.kekmech.common_images.imagepicker.source.ImageSource
import vivid.money.elmslie.core.store.Actor

internal class ImagePickerActor(
    private val imageSource: ImageSource
) : Actor<ImagePickerAction, ImagePickerEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: ImagePickerAction): Observable<ImagePickerEvent> = when (action) {
        is ImagePickerAction.LoadImages -> imageSource
            .getImagesUrls(action.page)
            .mapSuccessEvent(News::ImagesLoaded)
    }
}