package com.helloumi.ui.utils.camera

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import com.helloumi.ui.R
import com.helloumi.ui.feature.camera.CameraFragment.Companion.FILENAME_FORMAT
import com.helloumi.ui.feature.camera.CameraFragment.Companion.PHOTO_EXTENSION
import com.helloumi.ui.feature.camera.CameraFragment.Companion.TAG
import java.util.Locale

object CameraFileUtils {

    fun takePhoto(
        context: Context,
        imageCapture: ImageCapture?,
        onImageCaptured: (Uri) -> Unit
    ) {
        // Get a stable reference of the modifiable image capture use case
        imageCapture ?: return

        val outputDirectory = createPhotoFile(context)


        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT,
                Locale.US
            ).format(System.currentTimeMillis()) + PHOTO_EXTENSION
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // On successful capture, invoke callback with the Uri of the saved file
                    Uri.fromFile(photoFile).let(onImageCaptured)
                }
            })
    }

    // Helper function to create a file in the external storage directory for the photo
    private fun createPhotoFile(context: Context): File {
        // Attempt to use the app-specific external storage directory which does not require permissions
        val mediaDir = context.getExternalFilesDir(null)?.let {
            File(it, context.resources.getString(R.string.file_name)).apply { mkdirs() }
        }
        // Fallback to internal storage if the external directory is not available
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }
}