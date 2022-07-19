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
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.coreui.items.*
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.databinding.FragmentSelectMapTypeBinding
import kekmech.ru.feature_app_settings.screens.map_type.dto.MapTypeEntry
import kekmech.ru.feature_app_settings.screens.map_type.item.MapTypeAdapterItem
import kekmech.ru.feature_app_settings.screens.map_type.item.MapTypeItem

internal class SelectMapTypeFragment : BottomSheetDialogFragment() {

    private val viewBinding by viewBinding(FragmentSelectMapTypeBinding::bind)
    private val adapter by fastLazy { createAdapter() }
    private val selectMapType by fastLazy { getArgument<String>(ARG_SELECTED_MAP_TYPE) }
    private val analytics by screenAnalytics("SelectMapType")
    private val resultKey by fastLazy { getArgument<String>(ARG_RESULT_KEY) }

    override val layoutId: Int = R.layout.fragment_select_map_type

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
        add(SectionHeaderItem(titleRes = R.string.change_map_type_screen_title))
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