package com.helloumi.data.repository

import com.helloumi.data.api.apc.ApcAPI
import com.helloumi.data.model.result.ProductsResult
import com.helloumi.domain.model.products.Product
import com.helloumi.domain.model.response.ProductsResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals

class ApcRepositoryImplTest {

    @Mock
    private lateinit var apcAPI: ApcAPI

    @InjectMocks
    lateinit var apcRepositoryImpl: ApcRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        apcRepositoryImpl = ApcRepositoryImpl(apcAPI)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN call getProducts THEN assert result`() {
        runTest {
            // GIVEN
            val products = listOf<Product>()
            val productsResponse: ProductsResponse = ProductsResponse(
                limit = 20,
                products = products,
                skip = 0,
                total = 100
            )

            Mockito.`when`(apcAPI.getProducts()).thenReturn(Response.success(productsResponse))

            // WHEN
            val result = apcRepositoryImpl.getProducts()

            // THEN
            result.mapLatest {
                assertEquals(
                    ProductsResult.Success(productsResponse),
                    it
                )
            }
        }
    }
}
