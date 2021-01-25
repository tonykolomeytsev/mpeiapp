package ru.kekmech.common_images.imagepicker

import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.ShowDialog
import ru.kekmech.common_images.launcher.ImagePickerLauncher

internal class ImagePickerLauncherImpl(
    private val router: Router
) : ImagePickerLauncher {

    override fun launch() {
        router.executeCommand(ShowDialog { ImagePickerFragment.newInstance() })
    }
}