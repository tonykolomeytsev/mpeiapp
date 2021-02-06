package ru.kekmech.common_images.navigation

import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import kekmech.ru.common_images.R
import kekmech.ru.common_navigation.Command
import ru.kekmech.common_images.imageviewer.ImageViewFragment

internal class NavigateWithSharedImage(
    private val transitionView: ImageView?,
    private val fragmentProvider: () -> ImageViewFragment
) : Command {

    override fun apply(supportFragmentManager: FragmentManager) = supportFragmentManager.commit {
        transitionView?.let { addSharedElement(it, it.transitionName) }
        setReorderingAllowed(true)
        add(R.id.container, fragmentProvider())
        addToBackStack(null)
    }
}