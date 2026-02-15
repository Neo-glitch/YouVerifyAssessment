package org.neo.yvstore.features.address.data.repository

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.database.dao.AddressDao
import org.neo.yvstore.core.database.model.AddressEntity
import org.neo.yvstore.core.domain.manager.UserManager
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.address.data.datasource.remote.AddressRemoteDatasource
import org.neo.yvstore.features.address.data.datasource.remote.model.AddressDto
import com.google.common.truth.Truth.assertThat

class AddressRepositoryImplUnitTest {

    private val remoteDatasource: AddressRemoteDatasource = mockk()
    private val addressDao: AddressDao = mockk()
    private val userManager: UserManager = mockk()
    private lateinit var repository: AddressRepositoryImpl

    private val user = User("u1", "a@b.com", "John", "Doe")
    private val entity = AddressEntity("a1", "u1", "123 Main", "City", "State", "Country")

    @Before
    fun setUp() {
        repository = AddressRepositoryImpl(remoteDatasource, addressDao, userManager)
    }

    // ── getAddresses ──

    @Test
    fun `getAddresses emits success with mapped addresses`() = runTest {
        every { addressDao.observeAllAddresses() } returns flowOf(listOf(entity))

        repository.getAddresses().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val addresses = (result as Resource.Success).data
            assertThat(addresses).hasSize(1)
            assertThat(addresses[0].id).isEqualTo("a1")
            awaitComplete()
        }
    }

    @Test
    fun `getAddresses emits error when dao throws`() = runTest {
        every { addressDao.observeAllAddresses() } returns flow {
            throw RuntimeException("db error")
        }

        repository.getAddresses().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
            awaitComplete()
        }
    }

    // ── getAddressById ──

    @Test
    fun `getAddressById returns success when found`() = runTest {
        coEvery { addressDao.getAddressById("a1") } returns entity

        val result = repository.getAddressById("a1")

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data.id).isEqualTo("a1")
    }

    @Test
    fun `getAddressById returns error when not found`() = runTest {
        coEvery { addressDao.getAddressById("a99") } returns null

        val result = repository.getAddressById("a99")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Address not found")
    }

    // ── addAddress ──

    @Test
    fun `addAddress returns success when user exists`() = runTest {
        coEvery { userManager.getUser() } returns user
        coEvery { remoteDatasource.addAddress("u1", any()) } returns "new-addr-id"
        coEvery { addressDao.insertAddress(any()) } returns Unit

        val address = Address("", "", "123 Main", "City", "State", "Country")
        val result = repository.addAddress(address)

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify { remoteDatasource.addAddress("u1", any()) }
        coVerify { addressDao.insertAddress(match { it.id == "new-addr-id" && it.userId == "u1" }) }
    }

    @Test
    fun `addAddress returns error when user is null`() = runTest {
        coEvery { userManager.getUser() } returns null

        val address = Address("", "", "123 Main", "City", "State", "Country")
        val result = repository.addAddress(address)

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("User not found")
    }

    @Test
    fun `addAddress returns error when remote throws`() = runTest {
        coEvery { userManager.getUser() } returns user
        coEvery { remoteDatasource.addAddress("u1", any()) } throws RuntimeException("network")

        val address = Address("", "", "123 Main", "City", "State", "Country")
        val result = repository.addAddress(address)

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
    }

    // ── deleteAddress ──

    @Test
    fun `deleteAddress deletes from remote then local`() = runTest {
        coEvery { remoteDatasource.deleteAddress("a1") } returns Unit
        coEvery { addressDao.deleteAddressById("a1") } returns Unit

        val result = repository.deleteAddress("a1")

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerifyOrder {
            remoteDatasource.deleteAddress("a1")
            addressDao.deleteAddressById("a1")
        }
    }

    @Test
    fun `deleteAddress returns error when remote throws`() = runTest {
        coEvery { remoteDatasource.deleteAddress("a1") } throws RuntimeException("network")

        val result = repository.deleteAddress("a1")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
    }

    // ── refreshAddresses ──

    @Test
    fun `refreshAddresses clears then inserts and returns success`() = runTest {
        val dto = AddressDto("a1", "u1", "123 Main", "City", "State", "Country")
        coEvery { userManager.getUser() } returns user
        coEvery { remoteDatasource.getAddresses("u1") } returns listOf(dto)
        coEvery { addressDao.deleteAllAddresses() } returns Unit
        coEvery { addressDao.insertAddresses(any()) } returns Unit

        val result = repository.refreshAddresses()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerifyOrder {
            addressDao.deleteAllAddresses()
            addressDao.insertAddresses(any())
        }
    }

    @Test
    fun `refreshAddresses returns error when user is null`() = runTest {
        coEvery { userManager.getUser() } returns null

        val result = repository.refreshAddresses()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("User not found")
    }

    @Test
    fun `refreshAddresses returns error when remote throws`() = runTest {
        coEvery { userManager.getUser() } returns user
        coEvery { remoteDatasource.getAddresses("u1") } throws RuntimeException("network")

        val result = repository.refreshAddresses()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
    }
}
