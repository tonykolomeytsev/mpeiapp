package kekmech.ru.update.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kekmech.ru.update.ForceUpdateFragmentPresenter
import kekmech.ru.update.R
import javax.inject.Inject

class ForceUpdateFragment : DaggerFragment(), ForceUpdateFragmentView {

    @Inject
    public var androidInjector: DispatchingAndroidInjector<Any>? = null

    @Inject
    lateinit var presenter: ForceUpdateFragmentPresenter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_force_update, container, false)
    }
}