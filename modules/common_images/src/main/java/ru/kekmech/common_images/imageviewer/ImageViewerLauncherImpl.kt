package ru.kekmech.common_images.imageviewer

import android.widget.ImageView
import kekmech.ru.common_navigation.Router
import ru.kekmech.common_images.launcher.ImageViewerLauncher
import ru.kekmech.common_images.navigation.NavigateWithSharedImage

internal class ImageViewerLauncherImpl(
    private val router: Router
) : ImageViewerLauncher {

    override fun launch(imageUrl: String, transitionImage: ImageView?) {
        router.executeCommand(NavigateWithSharedImage(transitionImage) {
            ImageViewFragment.newInstance(imageUrl, transitionImage?.transitionName)
        })
    }
}