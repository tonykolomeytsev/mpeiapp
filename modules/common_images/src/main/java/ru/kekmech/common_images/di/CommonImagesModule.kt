package ru.kekmech.common_images.di

import kekmech.ru.common_di.ModuleProvider
import org.koin.dsl.bind
import ru.kekmech.common_images.imagepicker.ImagePickerLauncherImpl
import ru.kekmech.common_images.imagepicker.mvi.ImagePickerFeatureFactory
import ru.kekmech.common_images.launcher.ImagePickerLauncher

object CommonImagesModule : ModuleProvider({
    factory { ImagePickerFeatureFactory() }
    factory { ImagePickerLauncherImpl(get()) } bind ImagePickerLauncher::class
})