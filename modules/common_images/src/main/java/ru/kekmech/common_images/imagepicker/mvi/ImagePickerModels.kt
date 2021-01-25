package ru.kekmech.common_images.imagepicker.mvi

import kekmech.ru.common_mvi.Feature

internal typealias ImagePickerFeature = Feature<ImagePickerState, ImagePickerEvent, ImagePickerEffect>

internal data class ImagePickerState(
    val isCollapsed: Boolean = false,
    val images: List<ImageItem> = emptyList(),
    val isStoragePermissionGranted: Boolean = false,
    val isCameraPermissionGranted: Boolean = false,
)

internal data class ImageItem(
    val url: String,
    val isSelected: Boolean
)

internal sealed class ImagePickerEvent {
    sealed class Wish : ImagePickerEvent() {
        object Init : Wish()

        object Action {
            object StoragePermissionGranted : Wish()
            object CameraPermissionGranted : Wish()
        }
    }
}

internal sealed class ImagePickerEffect

internal sealed class ImagePickerAction