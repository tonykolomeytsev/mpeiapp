package ru.kekmech.common_images.imagepicker

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.views.setMargins
import kekmech.ru.common_images.R
import kekmech.ru.common_images.databinding.FragmentImagePickerBinding
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.coreui.items.PullAdapterItem
import org.koin.android.ext.android.inject
import ru.kekmech.common_images.imagepicker.adapter.ImageAdapterItem
import ru.kekmech.common_images.imagepicker.adapter.ImageItem
import ru.kekmech.common_images.imagepicker.mvi.*
import ru.kekmech.common_images.imagepicker.mvi.ImagePickerEvent.Wish
import ru.kekmech.common_images.imagepicker.utils.requestStoragePermissionIfNeeded
import ru.kekmech.common_images.launcher.ImagePickerLauncher
import ru.kekmech.common_images.launcher.ImageViewerLauncher


private const val REQUEST_CODE_STORAGE = 1000
private const val REQUEST_CODE_CAMERA = 1001
private const val ARG_IMAGE_COUNT = "Arg.ImageCount"
private const val ARG_ALREADY_SELECTED_IMAGES = "Arg.Selected"

internal class ImagePickerFragment : BaseFragment<ImagePickerEvent, ImagePickerEffect, ImagePickerState, ImagePickerFeature>() {

    private val viewBinding by viewBinding(FragmentImagePickerBinding::bind)
    override val initEvent get() = Wish.Init
    override var layoutId: Int = R.layout.fragment_image_picker
    override fun createFeature() = inject<ImagePickerFeatureFactory>().value
        .create(getArgument(ARG_IMAGE_COUNT), getArgument(ARG_ALREADY_SELECTED_IMAGES))
    private val imageViewerLauncher by inject<ImageViewerLauncher>()
    private val adapter by lazy(LazyThreadSafetyMode.NONE) { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
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
            view.doOnApplyWindowInsets { _, insets, padding ->
                acceptButtonContainer.updatePadding(bottom = insets.systemWindowInsetBottom + padding.bottom)
                viewBinding.coordinatorLayout.setMargins(top = insets.systemWindowInsetTop + padding.top)
            }
            BottomSheetBehavior.from(recyclerView).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            if (isVisible) close()
                        }
                    }
                })
                recyclerView.postDelayed({
                    state = BottomSheetBehavior.STATE_COLLAPSED
                }, 100L)
            }
            overlayView.setOnClickListener { close() }
        }
        requestStoragePermissionIfNeeded(REQUEST_CODE_STORAGE) {
            feature.accept(Wish.Action.StoragePermissionGranted)
        }
    }

    override fun render(state: ImagePickerState) {
        adapter.update(ImagePickerListConverter.map(state))
        viewBinding.acceptButtonContainer.isVisible = state.isAcceptButtonVisible
    }

    override fun handleEffect(effect: ImagePickerEffect) = when (effect) {
        is ImagePickerEffect.CloseWithResult -> closeWithResult {
            putExtra(ImagePickerLauncher.EXTRA_SELECTED_IMAGES, effect.selectedImagesUrls)
        }
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
            onClickListener = { imageView, imageUrl ->
                imageViewerLauncher.launch(imageUrl, imageView)
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
                        if (adapter.allData[position] is ImageItem) {
                            return 1
                        } else {
                            return 3
                        }
                    }
                }
            }


    companion object {

        private const val DEFAULT_VISIBLE_THRESHOLD = 16 // 1 (PullItem) + 3 (span count) * 5 (rows)

        fun newInstance(
            imageCountLimit: Int,
            alreadySelectedImages: ArrayList<String>
        ): ImagePickerFragment = ImagePickerFragment()
            .withArguments(
                ARG_IMAGE_COUNT to imageCountLimit,
                ARG_ALREADY_SELECTED_IMAGES to alreadySelectedImages
            )
    }
}