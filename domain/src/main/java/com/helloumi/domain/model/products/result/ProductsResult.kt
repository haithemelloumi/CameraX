package com.helloumi.data.model.result

import com.helloumi.domain.model.response.ProductsResponse

/**
 * Current Products result, from web service requests.
 */
sealed class ProductsResult {

    /**
     * Success result.
     *
     * @property productsResponse downloaded products response.
     */
    class Success(val productsResponse: ProductsResponse) : ProductsResult()

    /**
     * Unavailable server result.
     */
    object ServerUnavailable : ProductsResult()

    /**
     * Error on server result.
     */
    object ServerError : ProductsResult()
}
