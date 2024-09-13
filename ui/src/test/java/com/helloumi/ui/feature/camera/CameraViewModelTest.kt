package com.helloumi.ui.feature.camera

import com.helloumi.data.model.result.ProductsResult
import com.helloumi.domain.usecases.GetProductsUseCase
import com.helloumi.ui.common.CoroutinesTestRule
import com.helloumi.ui.feature.cities.CameraViewModel
import com.helloumi.ui.utils.dispatchers.DispatcherProviderImpl
import com.helloumi.ui.utils.network.NetworkMonitor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CameraViewModelTest {

    @Mock
    lateinit var networkMonitor: NetworkMonitor

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var getProductsUseCase: GetProductsUseCase

    lateinit var viewModel: CameraViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = CameraViewModel(
            networkMonitor = networkMonitor,
            dispatcherProvider = DispatcherProviderImpl(
                main = coroutinesTestRule.testDispatcher,
                io = coroutinesTestRule.testDispatcher,
            ),
            getProductsUseCase = getProductsUseCase
        )
    }

    @Test
    fun `WHEN callLoading THEN verify networkMonitor and useCase`() {
        // THEN
        verify(networkMonitor).isOnline
        verify(getProductsUseCase).invoke()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN doGetProducts THEN assert values`(){
        runTest {
            // WHEN
            Mockito.`when`(getProductsUseCase.invoke()).thenReturn(flowOf(ProductsResult.ServerError))
            viewModel.doGetProducts()

            // THEN
            assertEquals(ProductsResult.ServerError, viewModel.productsUi.value)
            assertNotEquals(ProductsResult.ServerUnavailable, viewModel.productsUi.value)
        }
    }
}
