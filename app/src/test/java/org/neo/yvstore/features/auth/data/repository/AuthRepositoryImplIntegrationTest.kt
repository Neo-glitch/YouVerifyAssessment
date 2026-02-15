package org.neo.yvstore.features.auth.data.repository

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.auth.data.datasource.remote.model.UserDto
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.datasource.TestAuthRemoteDatasource


class AuthRepositoryImplIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: AuthRepositoryImpl
    private lateinit var testDatasource: TestAuthRemoteDatasource

    @Before
    fun setup() {
        testDatasource = TestAuthRemoteDatasource()
        repository = AuthRepositoryImpl(testDatasource)
    }

    @Test
    fun `signIn should return Success with mapped user when remote succeeds`() = runTest {
        // Arrange
        val userDto = UserDto(
            uid = "user-123",
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe"
        )
        testDatasource.signInResult = userDto

        // Act
        val result = repository.signIn("test@example.com", "password123")

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val successResult = result as Resource.Success
        assertThat(successResult.data.uid).isEqualTo("user-123")
        assertThat(successResult.data.email).isEqualTo("test@example.com")
        assertThat(successResult.data.firstName).isEqualTo("John")
        assertThat(successResult.data.lastName).isEqualTo("Doe")
    }

    @Test
    fun `signIn should return Error when remote throws exception`() = runTest {
        // Arrange
        testDatasource.signInError = Exception("Invalid credentials")

        // Act
        val result = repository.signIn("test@example.com", "wrongpassword")

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isEqualTo("An unexpected error occurred")
    }

    @Test
    fun `signUp should return Success when remote succeeds`() = runTest {
        // Arrange - no error configured means success

        // Act
        val result = repository.signUp(
            email = "new@example.com",
            password = "password123",
            firstName = "Jane",
            lastName = "Smith"
        )

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val successResult = result as Resource.Success
        assertThat(successResult.data).isEqualTo(Unit)
    }

    @Test
    fun `signUp should return Error when remote throws exception`() = runTest {
        // Arrange
        testDatasource.signUpError = Exception("Email already exists")

        // Act
        val result = repository.signUp(
            email = "existing@example.com",
            password = "password123",
            firstName = "Jane",
            lastName = "Smith"
        )

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val errorResult = result as Resource.Error
        assertThat(errorResult.message).isEqualTo("An unexpected error occurred")
    }
}
