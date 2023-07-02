package kekmech.ru.feature_app_settings.screens.map_type

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
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.databinding.FragmentSelectMapTypeBinding
import kekmech.ru.feature_app_settings.screens.map_type.dto.MapTypeEntry
import kekmech.ru.feature_app_settings.screens.map_type.item.MapTypeAdapterItem
import kekmech.ru.feature_app_settings.screens.map_type.item.MapTypeItem
import kekmech.ru.strings.Strings

internal class SelectMapTypeFragment : BottomSheetDialogFragment(R.layout.fragment_select_map_type) {

    private val viewBinding by viewBinding(FragmentSelectMapTypeBinding::bind)
    private val adapter by fastLazy { createAdapter() }
    private val selectMapType by fastLazy { getArgument<String>(ARG_SELECTED_MAP_TYPE) }
    private val analytics by screenAnalytics("SelectMapType")
    private val resultKey by fastLazy { getArgument<String>(ARG_RESULT_KEY) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }
        adapter.update(getItems())
    }

    private fun getItems() = mutableListOf<Any>().apply {
        add(PullItem)
        add(SpaceItem.VERTICAL_8)
        add(SectionHeaderItem(titleRes = Strings.change_map_type_screen_title))
        add(SpaceItem.VERTICAL_12)
        addAll(
            MapTypeEntry
                .values()
                .asList()
                .map { MapTypeItem(it, it.mapTypeCode == selectMapType) }
        )
        add(SpaceItem.VERTICAL_16)
    }

    private fun createAdapter() = BaseAdapter(
        SectionHeaderAdapterItem(),
        SpaceAdapterItem(),
        MapTypeAdapterItem {
            analytics.sendClick("SelectMapType_${it.mapTypeCode}")
            if (it.mapTypeCode == selectMapType) {
                close()
            } else {
                close()
                setResult(resultKey, result = it.mapTypeCode)
            }
        },
        PullAdapterItem()
    )

    companion object {

        private const val ARG_SELECTED_MAP_TYPE = "Arg.Type"
        private const val ARG_RESULT_KEY = "Arg.ResultKey"

        fun newInstance(selectedLanguage: String, resultKey: String) =
            SelectMapTypeFragment()
            .withArguments(
                ARG_SELECTED_MAP_TYPE to selectedLanguage,
                ARG_RESULT_KEY to resultKey
            )
    }
}
