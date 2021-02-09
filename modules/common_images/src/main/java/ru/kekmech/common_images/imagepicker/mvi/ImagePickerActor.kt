package ru.kekmech.common_images.imagepicker.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import ru.kekmech.common_images.imagepicker.mvi.ImagePickerEvent.News
import ru.kekmech.common_images.imagepicker.source.ImageSource

internal class ImagePickerActor(
    private val imageSource: ImageSource
) : Actor<ImagePickerAction, ImagePickerEvent> {

    override fun execute(action: ImagePickerAction): Observable<ImagePickerEvent> = when (action) {
        is ImagePickerAction.LoadImages -> imageSource
            .getImagesUrls(action.page)
            .mapSuccessEvent(News::ImagesLoaded)
    }
}