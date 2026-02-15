package org.neo.yvstore.features.auth.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.auth.domain.repository.AuthRepository
import com.google.common.truth.Truth.assertThat

class SignUpUseCaseUnitTest {

    private val repository: AuthRepository = mockk()
    private lateinit var useCase: SignUpUseCase

    @Before
    fun setUp() {
        useCase = SignUpUseCase(repository)
    }

    @Test
    fun `invoke should return success from repository`() = runTest {
        coEvery {
            repository.signUp("a@b.com", "pass123", "John", "Doe")
        } returns Resource.Success(Unit)

        val result = useCase(
            email = "a@b.com",
            password = "pass123",
            firstName = "John",
            lastName = "Doe"
        )

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify(exactly = 1) {
            repository.signUp("a@b.com", "pass123", "John", "Doe")
        }
    }

    @Test
    fun `invoke should return error from repository`() = runTest {
        coEvery {
            repository.signUp("a@b.com", "pass123", "John", "Doe")
        } returns Resource.Error("An account with this email already exists")

        val result = useCase(
            email = "a@b.com",
            password = "pass123",
            firstName = "John",
            lastName = "Doe"
        )

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("An account with this email already exists")
    }
}
