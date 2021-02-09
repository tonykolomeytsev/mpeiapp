package ru.kekmech.common_images.imagepicker.utils

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

internal fun Fragment.requestCameraPermissionIfNeeded(
    requestCode: Int,
    onPermissionAlreadyGranted: () -> Unit
) = requestPermissionIfNeeded(
    permission = android.Manifest.permission.CAMERA,
    requestCode = requestCode,
    onPermissionAlreadyGranted = onPermissionAlreadyGranted
)

internal fun Fragment.requestStoragePermissionIfNeeded(
    requestCode: Int,
    onPermissionAlreadyGranted: () -> Unit
) = requestPermissionIfNeeded(
    permission = android.Manifest.permission.READ_EXTERNAL_STORAGE,
    requestCode = requestCode,
    onPermissionAlreadyGranted = onPermissionAlreadyGranted
)

private fun Fragment.requestPermissionIfNeeded(
    permission: String,
    requestCode: Int,
    onPermissionAlreadyGranted: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= 23) {
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PERMISSION_GRANTED) {
            onPermissionAlreadyGranted()
        } else {
            requestPermissions(arrayOf(permission), requestCode)
        }
    } else {
        onPermissionAlreadyGranted()
    }
}
