package ru.kekmech.common_images.di

import kekmech.ru.common_di.ModuleProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import ru.kekmech.common_images.imagepicker.ImagePickerLauncherImpl
import ru.kekmech.common_images.imagepicker.mvi.ImagePickerActor
import ru.kekmech.common_images.imagepicker.mvi.ImagePickerFeatureFactory
import ru.kekmech.common_images.imagepicker.source.ImageSource
import ru.kekmech.common_images.imageviewer.ImageViewerLauncherImpl
import ru.kekmech.common_images.launcher.ImagePickerLauncher
import ru.kekmech.common_images.launcher.ImageViewerLauncher

object CommonImagesModule : ModuleProvider({
    factory { ImageSource(androidContext().contentResolver) }
    factory { ImagePickerActor(get()) }
    factory { ImagePickerFeatureFactory(get()) }
    factory { ImagePickerLauncherImpl(get()) } bind ImagePickerLauncher::class
    factory { ImageViewerLauncherImpl(get()) } bind ImageViewerLauncher::class
})