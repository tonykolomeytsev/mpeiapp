package ru.kekmech.common_images.imagepicker

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_images.R
import kekmech.ru.common_images.databinding.FragmentImagePickerBinding
import kekmech.ru.common_mvi.ui.BaseBottomSheetDialogFragment
import org.koin.android.ext.android.inject
import ru.kekmech.common_images.imagepicker.mvi.*
import ru.kekmech.common_images.imagepicker.mvi.ImagePickerEvent.Wish
import ru.kekmech.common_images.imagepicker.utils.requestStoragePermissionIfNeeded

private const val REQUEST_CODE_STORAGE = 1000
private const val REQUEST_CODE_CAMERA = 1001

internal class ImagePickerFragment : BaseBottomSheetDialogFragment<ImagePickerEvent, ImagePickerEffect, ImagePickerState, ImagePickerFeature>() {

    private val viewBinding by viewBinding(FragmentImagePickerBinding::bind)

    override val initEvent get() = Wish.Init

    override var layoutId: Int = R.layout.fragment_image_picker

    override fun createFeature() = inject<ImagePickerFeatureFactory>().value.create()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        requestStoragePermissionIfNeeded(REQUEST_CODE_STORAGE) {
            feature.accept(Wish.Action.StoragePermissionGranted)
        }
        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    override fun render(state: ImagePickerState) {
        /* no-op */
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_STORAGE -> if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                feature.accept(Wish.Action.StoragePermissionGranted)
            }
            REQUEST_CODE_CAMERA -> if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                feature.accept(Wish.Action.CameraPermissionGranted)
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun createAdapter() = BaseAdapter()

    companion object {

        fun newInstance(): ImagePickerFragment = ImagePickerFragment()
    }
}