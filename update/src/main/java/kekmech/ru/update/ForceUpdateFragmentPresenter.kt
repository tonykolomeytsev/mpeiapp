package kekmech.ru.update

import android.content.Context
import android.content.Intent
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.update.model.ForceUpdateFragmentModel
import kekmech.ru.update.view.ForceUpdateFragmentView
import javax.inject.Inject
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.ACTION_VIEW
import android.net.Uri


class ForceUpdateFragmentPresenter @Inject constructor(
    val model: ForceUpdateFragmentModel,
    private val router: Router,
    private val context: Context
) : Presenter<ForceUpdateFragmentView>() {

    override fun onResume(view: ForceUpdateFragmentView) {
        super.onResume(view)
        view.onUpdateNow = this::onUpdateNow
        view.onUpdateLater = router::popBackStack
    }

    private fun onUpdateNow() {
        val intent = Intent(ACTION_VIEW, Uri.parse(model.url))
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

        router.popBackStack()
    }
}