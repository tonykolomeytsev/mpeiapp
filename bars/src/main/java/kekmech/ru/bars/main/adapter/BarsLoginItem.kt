package kekmech.ru.bars.main.adapter

import android.content.Context
import android.graphics.Point
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import kekmech.ru.bars.R
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder2


class BarsLoginItem(
    val logInAction: (login: String, pass: String, showError: () -> Unit) -> Unit,
    val rightsAction: () -> Unit
) : BaseItem<BarsLoginItem.ViewHolder>() {

    var dumbError: () -> Unit = {}

    override fun updateViewHolder(viewHolder: ViewHolder) {

        viewHolder.apply {
            pass = ""
            setState(true)
            isErrorVisible = false

            dumbError = {
                setState(true)
            }

            rights = { rightsAction() }
            btlLogin = {
                setState(false)
                logInAction(login, pass) OnError@{ setState(true) }
            }
            textViewPass.setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                    // If the event is a key-down event on the "enter" button
                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        setState(false)
                        logInAction(login, pass) OnError@{ setState(true) }
                        return true
                    }
                    return false
                }
            })
        }
    }

    fun error() {
        dumbError()
    }

    private fun ViewHolder.setState(state: Boolean) {
        isErrorVisible = state
        isProgressBarVisible = !state
        isBtnLoginVisible = state
        isLoginEnabled = state
        isPassEnabled = state
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder2(view) {
        val login by bindText(R.id.textViewBarsLogin)
        var pass by bindText(R.id.textViewBarsPass)
        val textViewPass by bind<EditText>(R.id.textViewBarsPass)
        var isLoginEnabled by bindEnabled(R.id.textViewBarsLogin)
        var isPassEnabled by bindEnabled(R.id.textViewBarsPass)
        var btlLogin by bindClickable(R.id.buttonLogin)
        var isBtnLoginVisible by bindVisibility(R.id.buttonLogin)
        var rights by bindClickable(R.id.layoutRights)
        var isProgressBarVisible by bindVisibility(R.id.progressBarLogin, keepBounds = true)
        var isErrorVisible by bindVisibility(R.id.textViewError, keepBounds = true)
    }

    class Factory : BaseFactory(R.layout.item_bars_login, ::ViewHolder)

    override fun equals(other: Any?) = other is BarsLoginItem
    override fun hashCode() = javaClass.hashCode()
}