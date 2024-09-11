package com.helloumi.domain.repository

import com.helloumi.data.model.result.ProductsResult
import kotlinx.coroutines.flow.Flow

interface ApcRepository {

    /**
     * Requests products to the APC services.
     *
     * @return a ProductsResult Flow
     */
    fun getProducts(): Flow<ProductsResult>
}
