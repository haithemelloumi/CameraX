package com.helloumi.data.repository

import com.helloumi.data.api.apc.ApcAPI
import com.helloumi.data.model.result.ProductsResult
import com.helloumi.domain.model.response.ProductsResponse
import com.helloumi.domain.repository.ApcRepository
import retrofit2.Response
import java.io.IOException
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ApcRepositoryImpl @Inject constructor(private val apcAPI: ApcAPI) :
    ApcRepository {

    override fun getProducts() = flow {
        emit(
            when (
                val response = processCallDetails(apcAPI.getProducts())
            ) {
                is ProductsResponse -> ProductsResult.Success(response)
                is Int -> ProductsResult.ServerError
                else -> ProductsResult.ServerUnavailable
            }
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private fun processCallDetails(responseCall: Response<ProductsResponse>): Any? {
        return try {
            if (responseCall.isSuccessful) {
                responseCall.body()
            } else {
                responseCall.code()
            }
        } catch (_: IOException) {
            ProductsResult.ServerUnavailable
        }
    }

}
