package kekmech.ru.feature_app_settings.screens.lang

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.close
import kekmech.ru.common_android.fragment.BottomSheetDialogFragment
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.setResult
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.withArguments
import kekmech.ru.coreui.items.PullAdapterItem
import kekmech.ru.coreui.items.PullItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.coreui.items.TextAdapterItem
import kekmech.ru.coreui.items.TextItem
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.databinding.FragmentChangeLanguageBinding
import kekmech.ru.feature_app_settings.screens.lang.dto.LanguageEntry
import kekmech.ru.feature_app_settings.screens.lang.item.LanguageAdapterItem
import kekmech.ru.feature_app_settings.screens.lang.item.LanguageItem
import kekmech.ru.strings.Strings
import kekmech.ru.coreui.R as coreui_R

internal class SelectLanguageFragment : BottomSheetDialogFragment(R.layout.fragment_change_language) {

    private val viewBinding by viewBinding(FragmentChangeLanguageBinding::bind)
    private val adapter by fastLazy { createAdapter() }
    private val selectLanguage by fastLazy { getArgument<String>(ARG_SELECTED_LANG) }
    private val analytics by screenAnalytics("SelectLanguage")
    private val resultKey by fastLazy { getArgument<String>(ARG_RESULT_KEY) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }
        adapter.update(getItems())
    }

    private fun getItems() =
        mutableListOf<Any>().apply {
            add(PullItem)
            add(SpaceItem.VERTICAL_8)
            add(SectionHeaderItem(titleRes = Strings.change_language_screen_title))
            add(SpaceItem.VERTICAL_12)
            addAll(
                LanguageEntry
                    .values()
                    .asList()
                    .map { LanguageItem(it, it.languageCode == selectLanguage) }
            )
            add(SpaceItem.VERTICAL_16)
            add(
                TextItem(
                    textResId = Strings.change_language_disclaimer,
                    styleResId = coreui_R.style.H6_Gray70
                )
            )
        }

    private fun createAdapter() =
        BaseAdapter(
            SectionHeaderAdapterItem(),
            SpaceAdapterItem(),
            TextAdapterItem(),
            LanguageAdapterItem {
                analytics.sendClick("SelectLanguage_${it.languageCode}")
                if (it.languageCode == selectLanguage) {
                    close()
                } else {
                    close()
                    setResult(resultKey, result = it.languageCode)
                }
            },
            PullAdapterItem()
        )

    companion object {

        private const val ARG_SELECTED_LANG = "Arg.Lang"
        private const val ARG_RESULT_KEY = "Arg.ResultKey"

        fun newInstance(selectedLanguage: String, resultKey: String) =
            SelectLanguageFragment()
                .withArguments(
                    ARG_SELECTED_LANG to selectedLanguage,
                    ARG_RESULT_KEY to resultKey
                )
    }
}
