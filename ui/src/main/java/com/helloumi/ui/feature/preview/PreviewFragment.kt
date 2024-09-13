package com.helloumi.ui.feature.preview

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.Fragment
import androidx.core.view.drawToBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import com.helloumi.data.model.result.ProductsResult
import com.helloumi.ui.R
import com.helloumi.ui.feature.cities.CameraViewModel
import com.helloumi.ui.feature.camera.CameraFragment.Companion.PHOTO_EXTENSION

/**
 * Preview Fragment
 */
class PreviewFragment : Fragment() {

    private lateinit var previewImage: AppCompatImageView

    private val viewModel: CameraViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPreviewImage()
        setProductData()
        setDownloadButtonListener()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private fun setPreviewImage() {
        previewImage = requireActivity().findViewById<AppCompatImageView>(R.id.preview_image)
        previewImage.setImageURI(viewModel.uri.value)
    }

    private fun setProductData() {
        val productsValue = viewModel.productsUi.value
        when (productsValue) {
            is ProductsResult.Success -> {
                val product = productsValue.productsResponse.products.first()
                setText(R.id.preview_title_text, product.title)
                setText(R.id.preview_price_text, String.format("${product.price}€"))
                setText(R.id.preview_description_text, product.description)
            }

            ProductsResult.ServerError, ProductsResult.ServerUnavailable -> {
                val noData = requireActivity().getString(R.string.no_data)
                setText(R.id.preview_title_text, noData)
                setText(R.id.preview_price_text, noData)
                setText(R.id.preview_description_text, noData)
            }
        }
    }

    private fun setDownloadButtonListener() {
        requireActivity().findViewById<Button>(R.id.preview_download_button).setOnClickListener {
            val bitmap: Bitmap = previewImage.drawToBitmap()
            saveImageToStorage(bitmap)
        }
    }

    private fun saveImageToStorage(bitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}$PHOTO_EXTENSION"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requireContext().contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(requireContext(), "Saved to Gallery", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setText(previewTextRes: Int, text: String) {
        val previewText = requireActivity().findViewById<TextView>(previewTextRes)
        previewText.text = text
    }
}
