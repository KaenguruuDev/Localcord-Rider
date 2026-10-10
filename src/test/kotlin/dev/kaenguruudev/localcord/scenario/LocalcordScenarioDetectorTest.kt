package dev.kaenguruudev.localcord.scenario

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalcordScenarioDetectorTest {
    @Test
    fun recognizesFirstLineDeclaration() {
        assertTrue(LocalcordScenarioDetector.startsWithScenarioDeclaration("scenario: ping\n"))
        assertTrue(LocalcordScenarioDetector.startsWithScenarioDeclaration("  scenario: ping\n"))
        assertFalse(
            LocalcordScenarioDetector.startsWithScenarioDeclaration("name: ping\nscenario: ping")
        )
    }
}
