package ru.kekmech.common_images.imagepicker

import androidx.fragment.app.Fragment
import kekmech.ru.common_android.withResultFor
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.ShowDialog
import ru.kekmech.common_images.launcher.ImagePickerLauncher

internal class ImagePickerLauncherImpl(
    private val router: Router
) : ImagePickerLauncher {


    override fun launch(requestCode: Int, targetFragment: Fragment, imageCountLimit: Int) {
        router.executeCommand(ShowDialog {
            ImagePickerFragment
                .newInstance(imageCountLimit)
                .withResultFor(targetFragment, requestCode)
        })
    }
}