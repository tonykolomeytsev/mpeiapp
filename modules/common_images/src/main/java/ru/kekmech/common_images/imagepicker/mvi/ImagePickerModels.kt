package ru.kekmech.common_images.imagepicker.mvi

import kekmech.ru.common_mvi.Feature

internal typealias ImagePickerFeature = Feature<ImagePickerState, ImagePickerEvent, ImagePickerEffect>

internal data class ImagePickerState(
    val isCollapsed: Boolean = false,
    val imagesUrls: Set<String> = emptySet(),
    val selectedImagesUrls: Set<String> = emptySet(),
    val isStoragePermissionGranted: Boolean = false,
    val isCameraPermissionGranted: Boolean = false,
    val imageCountLimit: Int,
    val loaderPage: Int = 0,
    val isLoading: Boolean = false
)

internal sealed class ImagePickerEvent {
    sealed class Wish : ImagePickerEvent() {
        object Init : Wish()

        object Action {
            object StoragePermissionGranted : Wish()
            object CameraPermissionGranted : Wish()
            object BottomReached : Wish()
        }

        object Click {
            data class Image(val imageUrl: String) : Wish()
            data class SelectImage(val imageUrl: String) : Wish()
        }
    }

    sealed class  News : ImagePickerEvent() {
        data class ImagesLoaded(val imagesUrls: List<String>): News()
    }
}

internal sealed class ImagePickerEffect

internal sealed class ImagePickerAction {
    data class LoadImages(val page: Int) : ImagePickerAction()
}