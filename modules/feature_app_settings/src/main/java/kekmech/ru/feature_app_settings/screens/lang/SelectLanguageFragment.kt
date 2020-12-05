package kekmech.ru.feature_app_settings.screens.lang

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.*
import kekmech.ru.common_android.fragment.BottomSheetDialogFragment
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.coreui.items.*
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.databinding.FragmentChangeLanguageBinding
import kekmech.ru.feature_app_settings.screens.lang.dto.LanguageEntry
import kekmech.ru.feature_app_settings.screens.lang.item.LanguageAdapterItem
import kekmech.ru.feature_app_settings.screens.lang.item.LanguageItem

private const val ARG_SELECTED_LANG = "Arg.Lang"

internal class SelectLanguageFragment : BottomSheetDialogFragment(R.layout.fragment_change_language) {

    private val viewBinding by viewBinding(FragmentChangeLanguageBinding::bind)
    private val adapter by fastLazy { createAdapter() }
    private val selectLanguage by fastLazy { getArgument<String>(ARG_SELECTED_LANG) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }
        adapter.update(getItems())
    }

    private fun getItems() = mutableListOf<Any>().apply {
        add(SpaceItem.VERTICAL_8)
        add(SectionHeaderItem(titleRes = R.string.change_language_screen_title))
        add(SpaceItem.VERTICAL_12)
        addAll(LanguageEntry.values().asList().map { LanguageItem(it, it.languageCode == selectLanguage) })
        add(SpaceItem.VERTICAL_16)
        add(TextItem(
            textResId = R.string.change_language_disclaimer,
            styleResId = R.style.H6_C8
        ))
    }

    private fun createAdapter() = BaseAdapter(
        SectionHeaderAdapterItem(),
        SpaceAdapterItem(),
        TextAdapterItem(),
        LanguageAdapterItem {
            if (it.languageCode == selectLanguage) {
                closeWithSuccess()
            } else {
                closeWithResult {
                    putExtra("app_lang", it.languageCode)
                }
            }
        }
    )

    companion object {

        fun newInstance(selectedLanguage: String) = SelectLanguageFragment()
            .withArguments(ARG_SELECTED_LANG to selectedLanguage)
    }
}