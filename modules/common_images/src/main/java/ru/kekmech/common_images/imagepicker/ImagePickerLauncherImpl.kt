package ru.kekmech.common_images.imagepicker

import androidx.fragment.app.Fragment
import kekmech.ru.common_android.withResultFor
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.ShowDialog
import ru.kekmech.common_images.launcher.ImagePickerLauncher

internal class ImagePickerLauncherImpl(
    private val router: Router
) : ImagePickerLauncher {


    override fun launch(
        requestCode: Int,
        targetFragment: Fragment,
        imageCountLimit: Int,
        alreadySelectedImages: List<String>
    ) {
        router.executeCommand(AddScreenForward {
            ImagePickerFragment
                .newInstance(imageCountLimit, ArrayList(alreadySelectedImages))
                .withResultFor(targetFragment, requestCode)
        })
    }
}