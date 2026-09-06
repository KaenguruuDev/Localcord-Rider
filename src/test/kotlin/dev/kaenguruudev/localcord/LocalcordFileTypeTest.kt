package dev.kaenguruudev.localcord

import org.jetbrains.yaml.YAMLLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class LocalcordFileTypeTest {
    @Test
    fun scenarioFileTypeUsesLocalcordLanguage() {
        val fileType = LocalcordScenarioFileType.INSTANCE

        assertEquals("Localcord Scenario", fileType.name)
        assertEquals("lc", fileType.defaultExtension)
        assertSame(LocalcordLanguage, fileType.language)
    }

    @Test
    fun fixtureFileTypeUsesYamlLanguage() {
        val fileType = LocalcordFixtureFileType.INSTANCE

        assertEquals("Localcord Fixture", fileType.name)
        assertEquals("lcf", fileType.defaultExtension)
        assertSame(YAMLLanguage.INSTANCE, fileType.language)
    }
}
