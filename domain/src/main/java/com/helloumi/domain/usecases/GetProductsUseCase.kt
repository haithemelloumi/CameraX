package com.helloumi.domain.usecases

import com.helloumi.domain.repository.ApcRepository
import javax.inject.Inject

/**
 * Gets Products.
 */
class GetProductsUseCase @Inject constructor(private val apcRepository: ApcRepository) {

    /**
     * Invokes use case.
     *
     * @return the current products result Flow.
     */
    operator fun invoke() = apcRepository.getProducts()
}
