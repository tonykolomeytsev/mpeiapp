package ru.kekmech.common_images.imagepicker.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

internal class ImagePickerReducer : BaseReducer<ImagePickerState, ImagePickerEvent, ImagePickerEffect, ImagePickerAction> {

    override fun reduce(
        event: ImagePickerEvent,
        state: ImagePickerState
    ): Result<ImagePickerState, ImagePickerEffect, ImagePickerAction> {
        return Result(state)
    }
}