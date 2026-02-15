package org.neo.yvstore.features.address.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.data.UserManagerImpl
import org.neo.yvstore.core.database.model.AddressEntity
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.address.data.datasource.remote.model.AddressDto
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.TestAppCache
import org.neo.yvstore.testdoubles.dao.TestAddressDao
import org.neo.yvstore.testdoubles.datasource.TestAddressRemoteDatasource

class AddressRepositoryImplIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: AddressRepositoryImpl
    private lateinit var testDatasource: TestAddressRemoteDatasource
    private lateinit var testDao: TestAddressDao
    private lateinit var userManager: UserManagerImpl
    private lateinit var testCache: TestAppCache

    @Before
    fun setup() {
        testDatasource = TestAddressRemoteDatasource()
        testDao = TestAddressDao()
        testCache = TestAppCache()
        userManager = UserManagerImpl(testCache)
        repository = AddressRepositoryImpl(testDatasource, testDao, userManager)
    }

    @Test
    fun `getAddresses should emit mapped addresses from dao`() = runTest {
        // Arrange
        val entities = listOf(
            createTestAddressEntity(
                id = "address-1",
                userId = "user-123",
                streetAddress = "123 Main St",
                city = "New York"
            ),
            createTestAddressEntity(
                id = "address-2",
                userId = "user-123",
                streetAddress = "456 Elm St",
                city = "Boston"
            )
        )
        testDao.insertAddresses(entities)

        // Act & Assert
        repository.getAddresses().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val successResult = result as Resource.Success
            assertThat(successResult.data).hasSize(2)
            assertThat(successResult.data[0].streetAddress).isEqualTo("123 Main St")
            assertThat(successResult.data[0].city).isEqualTo("New York")
            assertThat(successResult.data[1].streetAddress).isEqualTo("456 Elm St")
            assertThat(successResult.data[1].city).isEqualTo("Boston")
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getAddressById should return mapped address when found`() = runTest {
        // Arrange
        val entity = createTestAddressEntity(
            id = "address-1",
            streetAddress = "789 Oak Ave",
            city = "Chicago"
        )
        testDao.insertAddress(entity)

        // Act
        val result = repository.getAddressById("address-1")

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val successResult = result as Resource.Success
        assertThat(successResult.data.id).isEqualTo("address-1")
        assertThat(successResult.data.streetAddress).isEqualTo("789 Oak Ave")
        assertThat(successResult.data.city).isEqualTo("Chicago")
    }

    @Test
    fun `getAddressById should return Error when not found`() = runTest {
        // Arrange - empty DAO

        // Act
        val result = repository.getAddressById("non-existent-id")

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isEqualTo("Address not found")
    }

    @Test
    fun `addAddress should create remote then insert local when user logged in`() = runTest {
        // Arrange - save logged-in user
        val user = createTestUser(uid = "user-123")
        userManager.saveUser(user)

        val address = createTestAddress(
            streetAddress = "101 Pine St",
            city = "Seattle"
        )
        testDatasource.addAddressResult = "new-address-id"

        // Act
        val result = repository.addAddress(address)

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)

        // Verify address inserted into DAO with correct ID and userId
        val savedAddress = testDao.getAddressById("new-address-id")
        assertThat(savedAddress).isNotNull()
        assertThat(savedAddress?.id).isEqualTo("new-address-id")
        assertThat(savedAddress?.userId).isEqualTo("user-123")
        assertThat(savedAddress?.streetAddress).isEqualTo("101 Pine St")
        assertThat(savedAddress?.city).isEqualTo("Seattle")
    }

    @Test
    fun `addAddress should return Error when user not logged in`() = runTest {
        // Arrange - no user saved

        val address = createTestAddress(
            streetAddress = "101 Pine St",
            city = "Seattle"
        )

        // Act
        val result = repository.addAddress(address)

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isEqualTo("User not found")

        // Verify no address inserted
        val addresses = testDao.getAddressById("any-id")
        assertThat(addresses).isNull()
    }

    @Test
    fun `deleteAddress should delete from remote then local`() = runTest {
        // Arrange - pre-populate DAO
        val entity = createTestAddressEntity(id = "address-1", streetAddress = "123 Main St")
        testDao.insertAddress(entity)

        // Act
        val result = repository.deleteAddress("address-1")

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)

        // Verify address removed from DAO
        val deletedAddress = testDao.getAddressById("address-1")
        assertThat(deletedAddress).isNull()
    }

    @Test
    fun `refreshAddresses should replace all local with remote data when user logged in`() = runTest {
        // Arrange - save logged-in user
        val user = createTestUser(uid = "user-123")
        userManager.saveUser(user)

        // Pre-populate DAO with old data
        testDao.insertAddress(createTestAddressEntity(id = "old-address", userId = "user-123", streetAddress = "Old Address St"))

        // Configure remote with new data
        val remoteAddresses = listOf(
            createTestAddressDto(id = "address-1", userId = "user-123", streetAddress = "New Address 1"),
            createTestAddressDto(id = "address-2", userId = "user-123", streetAddress = "New Address 2")
        )
        testDatasource.addresses = remoteAddresses

        // Act
        val result = repository.refreshAddresses()

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)

        // Verify old address removed
        val oldAddress = testDao.getAddressById("old-address")
        assertThat(oldAddress).isNull()

        // Verify new addresses inserted
        val newAddress1 = testDao.getAddressById("address-1")
        assertThat(newAddress1).isNotNull()
        assertThat(newAddress1?.streetAddress).isEqualTo("New Address 1")

        val newAddress2 = testDao.getAddressById("address-2")
        assertThat(newAddress2).isNotNull()
        assertThat(newAddress2?.streetAddress).isEqualTo("New Address 2")
    }

    @Test
    fun `refreshAddresses should return Error when user not logged in`() = runTest {
        // Arrange - no user saved

        // Act
        val result = repository.refreshAddresses()

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isEqualTo("User not found")
    }

    // Helper functions to create test data
    private fun createTestUser(
        uid: String = "test-user-id",
        email: String = "test@example.com",
        firstName: String = "John",
        lastName: String = "Doe"
    ) = User(
        uid = uid,
        email = email,
        firstName = firstName,
        lastName = lastName
    )

    private fun createTestAddress(
        id: String = "",
        userId: String = "",
        streetAddress: String,
        city: String,
        state: String = "CA",
        country: String = "USA"
    ) = Address(
        id = id,
        userId = userId,
        streetAddress = streetAddress,
        city = city,
        state = state,
        country = country
    )

    private fun createTestAddressEntity(
        id: String,
        userId: String = "user-123",
        streetAddress: String,
        city: String = "Test City",
        state: String = "CA",
        country: String = "USA"
    ) = AddressEntity(
        id = id,
        userId = userId,
        streetAddress = streetAddress,
        city = city,
        state = state,
        country = country
    )

    private fun createTestAddressDto(
        id: String,
        userId: String,
        streetAddress: String,
        city: String = "Test City",
        state: String = "CA",
        country: String = "USA"
    ) = AddressDto(
        id = id,
        userId = userId,
        streetAddress = streetAddress,
        city = city,
        state = state,
        country = country
    )
}
