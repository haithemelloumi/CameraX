package com.helloumi.domain.model.response

import com.helloumi.domain.model.products.Product

data class ProductsResponse(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)