package kekmech.ru.common_navigation_api

import android.os.Parcelable
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node

abstract class NavTarget : Parcelable {

    val key: String? = null

    abstract fun resolve(buildContext: BuildContext): Node
}
