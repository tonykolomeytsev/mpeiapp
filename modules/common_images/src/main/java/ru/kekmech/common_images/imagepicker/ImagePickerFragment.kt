package ru.kekmech.common_images.imagepicker

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.closeWithResult
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_images.R
import kekmech.ru.common_images.databinding.FragmentImagePickerBinding
import kekmech.ru.common_mvi.ui.BaseBottomSheetDialogFragment
import kekmech.ru.common_navigation.showDialog
import kekmech.ru.coreui.items.PullAdapterItem
import kekmech.ru.coreui.items.PullItem
import org.koin.android.ext.android.inject
import ru.kekmech.common_images.imagepicker.adapter.ImageAdapterItem
import ru.kekmech.common_images.imagepicker.mvi.*
import ru.kekmech.common_images.imagepicker.mvi.ImagePickerEvent.Wish
import ru.kekmech.common_images.imagepicker.utils.BottomSheetFixedElementHelper
import ru.kekmech.common_images.imagepicker.utils.requestStoragePermissionIfNeeded
import ru.kekmech.common_images.imageviewer.ImageViewFragment
import ru.kekmech.common_images.launcher.ImagePickerLauncher


private const val REQUEST_CODE_STORAGE = 1000
private const val REQUEST_CODE_CAMERA = 1001
private const val ARG_IMAGE_COUNT = "Arg.ImageCount"

internal class ImagePickerFragment : BaseBottomSheetDialogFragment<ImagePickerEvent, ImagePickerEffect, ImagePickerState, ImagePickerFeature>() {

    private val viewBinding by viewBinding(FragmentImagePickerBinding::bind)
    override val initEvent get() = Wish.Init
    override var layoutId: Int = R.layout.fragment_image_picker
    override fun createFeature() = inject<ImagePickerFeatureFactory>().value
        .create(getArgument(ARG_IMAGE_COUNT))

    private val adapter by lazy(LazyThreadSafetyMode.NONE) { createAdapter() }
    private lateinit var bottomSheetFixedElementHelper: BottomSheetFixedElementHelper

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        requestStoragePermissionIfNeeded(REQUEST_CODE_STORAGE) {
            feature.accept(Wish.Action.StoragePermissionGranted)
        }
        with(viewBinding) {
            recyclerView.adapter = adapter
            val gridLayoutManager = createLayoutManager()
            recyclerView.layoutManager = gridLayoutManager
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy <= 0) return
                    if (adapter.itemCount <= gridLayoutManager.findLastVisibleItemPosition() + DEFAULT_VISIBLE_THRESHOLD) {
                        feature.accept(Wish.Action.BottomReached)
                    }
                }
            })
            buttonAccept.setOnClickListener {
                feature.accept(Wish.Click.Accept)
            }
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    println("Scroll hello world")
                }
            })
            bottomSheetFixedElementHelper = BottomSheetFixedElementHelper(acceptButtonContainer, requireActivity().window)
        }
        requestStoragePermissionIfNeeded(REQUEST_CODE_STORAGE) {
            feature.accept(Wish.Action.StoragePermissionGranted)
        }
    }

    override fun render(state: ImagePickerState) {
        adapter.update(ImagePickerListConverter.map(state))
        viewBinding.buttonAccept.isVisible = state.isAcceptButtonVisible
        (view?.parent as? View)?.let(bottomSheetFixedElementHelper::update)
    }

    override fun handleEffect(effect: ImagePickerEffect) = when (effect) {
        is ImagePickerEffect.CloseWithResult -> closeWithResult {
            putExtra(ImagePickerLauncher.EXTRA_SELECTED_IMAGES, effect.selectedImagesUrls)
        }
        is ImagePickerEffect.ShowImage -> {
            showDialog {
                ImageViewFragment.newInstance(effect.url)
            }
        }
    }

    override fun onBottomSheetSlide(bottomSheet: View, slideOffset: Float) {
        bottomSheetFixedElementHelper.update(bottomSheet)
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

    private fun createAdapter() = BaseAdapter(
        ImageAdapterItem(
            onClickListener = { imageUrl ->
                feature.accept(Wish.Click.Image(imageUrl))
            },
            onSelectListener = { imageUrl ->
                feature.accept(Wish.Click.SelectImage(imageUrl))
            }
        ),
        PullAdapterItem()
    )

    private fun createLayoutManager() =
        GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
            .apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        if (adapter.allData[position] is PullItem) {
                            return 3
                        } else {
                            return 1
                        }
                    }
                }
            }


    companion object {

        private const val DEFAULT_VISIBLE_THRESHOLD = 16 // 1 (PullItem) + 3 (span count) * 5 (rows)

        fun newInstance(imageCountLimit: Int): ImagePickerFragment = ImagePickerFragment()
            .withArguments(ARG_IMAGE_COUNT to imageCountLimit)
    }
}