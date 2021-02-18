package ru.kekmech.common_images.imagepicker.elm

import ru.kekmech.common_images.imagepicker.elm.ImagePickerEvent.News
import ru.kekmech.common_images.imagepicker.elm.ImagePickerEvent.Wish
import vivid.money.elmslie.core.store.StateReducer
import vivid.money.elmslie.core.store.Result

internal class ImagePickerReducer :
    StateReducer<ImagePickerEvent, ImagePickerState, ImagePickerEffect, ImagePickerAction> {

    override fun reduce(
        event: ImagePickerEvent,
        state: ImagePickerState
    ): Result<ImagePickerState, ImagePickerEffect, ImagePickerAction> = when (event) {
        is Wish -> handleWish(event, state)
        is News -> handleNews(event, state)
    }

    private fun handleNews(
        event: News,
        state: ImagePickerState
    ): Result<ImagePickerState, ImagePickerEffect, ImagePickerAction> = when (event) {
        is News.ImagesLoaded -> Result(
            state = state.copy(
                imagesUrls = state.imagesUrls + event.imagesUrls,
                loaderPage = if (event.imagesUrls.isNotEmpty()) state.loaderPage + 1 else state.loaderPage,
                isLoading = false
            )
        )
    }

    private fun handleWish(
        event: Wish,
        state: ImagePickerState
    ): Result<ImagePickerState, ImagePickerEffect, ImagePickerAction> = when (event) {
        is Wish.Init -> Result(state)
        is Wish.Action.StoragePermissionGranted -> Result(
            state = state.copy(isStoragePermissionGranted = true),
            command = ImagePickerAction.LoadImages(state.loaderPage)
                .takeIf { !state.isStoragePermissionGranted }
        )
        is Wish.Action.BottomReached -> {
            if (!state.isLoading) {
                Result(
                    state = state.copy(isLoading = true),
                    command = ImagePickerAction.LoadImages(state.loaderPage)
                )
            } else {
                Result(state)
            }
        }
        is Wish.Click.SelectImage -> {
            if (state.selectedImagesUrls.size < state.imageCountLimit) {
                val newSelectedImages = if (event.imageUrl in state.selectedImagesUrls) {
                    state.selectedImagesUrls - event.imageUrl
                } else {
                    state.selectedImagesUrls + event.imageUrl
                }
                Result(state.copy(selectedImagesUrls = newSelectedImages))
            } else {
                Result(state)
            }
        }
        is Wish.Click.Accept -> Result(
            state = state,
            effect = ImagePickerEffect.CloseWithResult(ArrayList(state.selectedImagesUrls))
        )
        is Wish.Action.CameraPermissionGranted -> Result(state) // TODO implement in the future
    }
}