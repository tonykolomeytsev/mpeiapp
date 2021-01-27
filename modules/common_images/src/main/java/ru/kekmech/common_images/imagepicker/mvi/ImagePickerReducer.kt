package ru.kekmech.common_images.imagepicker.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import ru.kekmech.common_images.imagepicker.mvi.ImagePickerEvent.News
import ru.kekmech.common_images.imagepicker.mvi.ImagePickerEvent.Wish

internal class ImagePickerReducer : BaseReducer<ImagePickerState, ImagePickerEvent, ImagePickerEffect, ImagePickerAction> {

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
                loaderPage = state.loaderPage + 1,
                isLoading = false
            )
        )
    }

    private fun handleWish(
        event: Wish,
        state: ImagePickerState
    ): Result<ImagePickerState, ImagePickerEffect, ImagePickerAction> = when (event) {
        is Wish.Init -> Result(
            state = state.copy(isLoading = true)
        )
        is Wish.Action.StoragePermissionGranted -> {
            val loaderPage = state.loaderPage
            Result(
                state = state.copy(
                    isStoragePermissionGranted = true,
                    isLoading = true
                ),
                action = ImagePickerAction.LoadImages(loaderPage)
            )
        }
        else -> Result(state)
    }
}