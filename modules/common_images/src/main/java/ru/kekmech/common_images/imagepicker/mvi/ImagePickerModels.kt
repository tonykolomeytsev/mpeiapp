package ru.kekmech.common_images.imagepicker.mvi

import kekmech.ru.common_mvi.Feature

internal typealias ImagePickerFeature = Feature<ImagePickerState, ImagePickerEvent, ImagePickerEffect>

internal data class ImagePickerState(
    val isCollapsed: Boolean = false,
    val imagesUrls: Set<String> = emptySet(),
    val selectedImagesUrls: Set<String> = emptySet(),
    val isStoragePermissionGranted: Boolean = false,
    val isCameraPermissionGranted: Boolean = false,
    val imageCountLimit: Int
)

internal sealed class ImagePickerEvent {
    sealed class Wish : ImagePickerEvent() {
        object Init : Wish()

        object Action {
            object StoragePermissionGranted : Wish()
            object CameraPermissionGranted : Wish()
        }

        object Click {
            data class Image(val imageUrl: String) : Wish()
        }
    }
}

internal sealed class ImagePickerEffect

internal sealed class ImagePickerAction