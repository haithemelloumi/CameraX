package com.helloumi.ui.feature.cities

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helloumi.data.model.result.ProductsResult
import com.helloumi.domain.usecases.GetProductsUseCase
import com.helloumi.ui.utils.dispatchers.DispatcherProvider
import com.helloumi.ui.utils.network.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Camera ViewModel.
 */
@HiltViewModel
class CameraViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val dispatcherProvider: DispatcherProvider,
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _productsUi: MutableStateFlow<ProductsResult> =
        MutableStateFlow(ProductsResult.ServerUnavailable)
    val productsUi: MutableStateFlow<ProductsResult> get() = _productsUi

    private val isOnline: Flow<Boolean> = networkMonitor.isOnline

    private val _isInternetAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isInternetAvailable: MutableStateFlow<Boolean> get() = _isInternetAvailable

    private val _uri = MutableStateFlow(Uri.EMPTY)
    val uri = _uri

    init {
        collectIsOnline()
        getProducts()
    }

    fun collectIsOnline() {
        viewModelScope.launch(dispatcherProvider.io) {
            isOnline.collectLatest {
                _isInternetAvailable.value = it
            }
        }
    }

    fun getProducts() {
        if (isInternetAvailable.value) {
            Log.i("getProducts:", "No Internet available")
        } else {
            doGetProducts()
        }
    }

    fun doGetProducts() {
        viewModelScope.launch(dispatcherProvider.io) {
            getProductsUseCase().collectLatest { it ->
                _productsUi.value = it
                when (it) {
                    is ProductsResult.Success -> Log.i(
                        TAG + SUCCESS,
                        it.productsResponse.toString()
                    )

                    is ProductsResult.ServerError -> {
                        Log.i(TAG, SERVER_ERROR)
                    }

                    is ProductsResult.ServerUnavailable -> {
                        Log.i(TAG, SERVER_UNAVAILABLE)
                    }
                }

            }
        }
    }

    fun updateUri(uri: Uri) {
        _uri.value = uri
    }

    companion object {
        private const val TAG = "getProducts:"
        private const val SUCCESS = "Success"
        private const val SERVER_ERROR = "ServerError"
        private const val SERVER_UNAVAILABLE = "ServerUnavailable"
    }
}
