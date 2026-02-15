package org.neo.yvstore.features.auth.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.auth.domain.repository.AuthRepository
import com.google.common.truth.Truth.assertThat

class LoginUseCaseUnitTest {

    private val repository: AuthRepository = mockk()
    private lateinit var useCase: LoginUseCase

    @Before
    fun setUp() {
        useCase = LoginUseCase(repository)
    }

    @Test
    fun `invoke returns success from repository`() = runTest {
        val user = User(uid = "1", email = "a@b.com", firstName = "John", lastName = "Doe")
        coEvery { repository.signIn("a@b.com", "pass123") } returns Resource.Success(user)

        val result = useCase(email = "a@b.com", password = "pass123")

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEqualTo(user)
        coVerify(exactly = 1) { repository.signIn("a@b.com", "pass123") }
    }

    @Test
    fun `invoke returns error from repository`() = runTest {
        coEvery { repository.signIn("a@b.com", "wrong") } returns Resource.Error("Invalid email or password")

        val result = useCase(email = "a@b.com", password = "wrong")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Invalid email or password")
    }
}
