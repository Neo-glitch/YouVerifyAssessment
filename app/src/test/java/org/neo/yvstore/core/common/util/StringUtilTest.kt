package org.neo.yvstore.core.common.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringUtilTest {

    @Test
    fun `capitalizeFirst should capitalize lowercase first character`() {
        assertThat("john".capitalizeFirst()).isEqualTo("John")
    }

    @Test
    fun `capitalizeFirst should keep already capitalized string unchanged`() {
        assertThat("John".capitalizeFirst()).isEqualTo("John")
    }

    @Test
    fun `capitalizeFirst should return empty string for empty input`() {
        assertThat("".capitalizeFirst()).isEqualTo("")
    }

    @Test
    fun `capitalizeFirst should only capitalize first character`() {
        assertThat("new york".capitalizeFirst()).isEqualTo("New york")
    }
}
