package com.helloumi.ui.feature.preview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.activityViewModels
import com.helloumi.data.model.result.ProductsResult
import com.helloumi.ui.R
import com.helloumi.ui.feature.cities.CameraViewModel

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

    private fun setText(previewTextRes: Int, text: String) {
        val previewText = requireActivity().findViewById<TextView>(previewTextRes)
        previewText.text = text
    }
}
