package org.neo.yvstore.features.address.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.domain.repository.AddressRepository
import com.google.common.truth.Truth.assertThat

class RefreshAddressesUseCaseUnitTest {

    private val repository: AddressRepository = mockk()
    private lateinit var useCase: RefreshAddressesUseCase

    @Before
    fun setUp() {
        useCase = RefreshAddressesUseCase(repository)
    }

    @Test
    fun `invoke returns success from repository`() = runTest {
        coEvery { repository.refreshAddresses() } returns Resource.Success(Unit)

        val result = useCase()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify(exactly = 1) { repository.refreshAddresses() }
    }

    @Test
    fun `invoke returns error from repository`() = runTest {
        coEvery { repository.refreshAddresses() } returns Resource.Error("User not found")

        val result = useCase()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("User not found")
    }
}
