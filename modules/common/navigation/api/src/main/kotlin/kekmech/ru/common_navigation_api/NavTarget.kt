package kekmech.ru.common_navigation_api

import android.os.Parcelable
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node

interface NavTarget : Parcelable {

    fun resolve(buildContext: BuildContext): Node
}
