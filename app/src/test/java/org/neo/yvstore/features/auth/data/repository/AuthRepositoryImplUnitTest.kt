package org.neo.yvstore.features.auth.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.auth.data.datasource.remote.AuthRemoteDatasource
import org.neo.yvstore.features.auth.data.datasource.remote.model.UserDto
import com.google.common.truth.Truth.assertThat

class AuthRepositoryImplUnitTest {

    private val remoteDatasource: AuthRemoteDatasource = mockk()
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        repository = AuthRepositoryImpl(remoteDatasource)
    }

    // ── signUp ──

    @Test
    fun `signUp should return success when datasource succeeds`() = runTest {
        coEvery {
            remoteDatasource.signUp("a@b.com", "pass123", "John", "Doe")
        } returns Unit

        val result = repository.signUp("a@b.com", "pass123", "John", "Doe")

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify(exactly = 1) {
            remoteDatasource.signUp("a@b.com", "pass123", "John", "Doe")
        }
    }

    @Test
    fun `signUp should return error when datasource throws`() = runTest {
        coEvery {
            remoteDatasource.signUp("a@b.com", "pass123", "John", "Doe")
        } throws RuntimeException("fail")

        val result = repository.signUp("a@b.com", "pass123", "John", "Doe")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
    }

    // ── signIn ──

    @Test
    fun `signIn should return success with mapped user`() = runTest {
        val dto = UserDto(uid = "1", email = "a@b.com", firstName = "John", lastName = "Doe")
        coEvery { remoteDatasource.signIn("a@b.com", "pass123") } returns dto

        val result = repository.signIn("a@b.com", "pass123")

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val user = (result as Resource.Success).data
        assertThat(user).isEqualTo(User("1", "a@b.com", "John", "Doe"))
    }

    @Test
    fun `signIn should return error when datasource throws`() = runTest {
        coEvery {
            remoteDatasource.signIn("a@b.com", "wrong")
        } throws RuntimeException("auth failed")

        val result = repository.signIn("a@b.com", "wrong")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An unexpected error occurred")
    }
}
