package com.helloumi.data.api.apc

import com.helloumi.domain.model.response.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApcAPI {

    @GET("/products")
    suspend fun getProducts(): Response<ProductsResponse>
}
