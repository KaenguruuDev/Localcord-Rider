package dev.kaenguruudev.localcord.run

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalcordRunConfigurationValidatorTest {
    @Test
    fun acceptsAbsoluteHttpAndHttpsUrls() {
        assertTrue(LocalcordRunConfigurationValidator.isHttpUrl("http://localhost:8080"))
        assertTrue(LocalcordRunConfigurationValidator.isHttpUrl("HTTPS://example.test/api"))
    }

    @Test
    fun rejectsNonHttpAndNonAbsoluteUrls() {
        assertFalse(LocalcordRunConfigurationValidator.isHttpUrl("localhost:8080"))
        assertFalse(LocalcordRunConfigurationValidator.isHttpUrl("ftp://example.test"))
        assertFalse(LocalcordRunConfigurationValidator.isHttpUrl("http://example.test/space here"))
    }

    @Test
    fun rejectsHttpUrlsWithoutAnAuthority() {
        assertFalse(LocalcordRunConfigurationValidator.isHttpUrl("http:foo"))
        assertFalse(LocalcordRunConfigurationValidator.isHttpUrl("https:///path"))
    }
}
