package com.helloumi.domain.usecases

import com.helloumi.data.model.result.ProductsResult
import com.helloumi.domain.model.response.ProductsResponse
import com.helloumi.domain.repository.ApcRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class GetProductsUseCaseTest {

    @Mock
    lateinit var apcRepository: ApcRepository

    @InjectMocks
    lateinit var getProductsUseCase: GetProductsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getProductsUseCase = GetProductsUseCase(
            apcRepository
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN invoke useCase THEN assert values and verify repository`() {
        // GIVEN
        val productsResponse = ProductsResponse(
            limit = 20,
            products = listOf(),
            skip = 5,
            total = 100
        )

        val flowResult = flowOf(ProductsResult.Success(productsResponse))

        Mockito.`when`(apcRepository.getProducts()).thenReturn(flowResult)

        // WHEN
        val result = getProductsUseCase.invoke()

        // THEN

        result.mapLatest {
            assertEquals(
                ProductsResult.Success(productsResponse),
                it
            )
        }

        verify(apcRepository).getProducts()
    }
}
