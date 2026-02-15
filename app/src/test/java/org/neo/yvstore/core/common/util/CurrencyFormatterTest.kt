package org.neo.yvstore.core.common.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CurrencyFormatterTest {

    @Test
    fun `formatAsPrice should format zero as dollar zero`() {
        assertThat(0.0.formatAsPrice()).isEqualTo("$0.00")
    }

    @Test
    fun `formatAsPrice should format whole number with two decimal places`() {
        assertThat(25.0.formatAsPrice()).isEqualTo("$25.00")
    }

    @Test
    fun `formatAsPrice should format fractional price correctly`() {
        assertThat(19.99.formatAsPrice()).isEqualTo("$19.99")
    }

    @Test
    fun `formatAsPrice should round to two decimal places`() {
        assertThat(9.999.formatAsPrice()).isEqualTo("$10.00")
    }
}
