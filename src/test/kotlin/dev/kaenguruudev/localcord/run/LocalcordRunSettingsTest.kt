package dev.kaenguruudev.localcord.run

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalcordRunSettingsTest {
    @Test
    fun resolvesRelativeOverridesFromScenarioDirectory() {
        val scenarioDirectory = File(File("localcord-rider-test"), "scenarios").absoluteFile
        val scenario = File(scenarioDirectory, "ping.lc")
        val settings =
            LocalcordRunSettings(
                scenarioPath = scenario.path,
                runnerConfigPath = "../localcord.runner.yaml",
                environmentFilePath = ".env.test",
            )

        assertEquals(
            File(scenarioDirectory, "../localcord.runner.yaml").absoluteFile,
            settings.runnerConfigFile(scenario),
        )
        assertEquals(
            File(scenarioDirectory, ".env.test").absoluteFile,
            settings.environmentFile(scenario),
        )
    }

    @Test
    fun blankFilterFallsBackToScenarioAndNonBlankFilterIsPreserved() {
        val scenario =
            File(File("localcord-rider-test"), File("scenarios", "ping.lc").path).absoluteFile

        assertEquals(
            scenario.absolutePath,
            LocalcordRunSettings(scenarioPath = scenario.path).effectiveFilter(scenario),
        )
        assertEquals(
            "smoke|ping",
            LocalcordRunSettings(filter = "smoke|ping").effectiveFilter(scenario),
        )
    }

    @Test
    fun runAllUsesAnEmptyEffectiveFilter() {
        val scenario = File("/workspace/scenarios/ping.lc")

        assertEquals("", LocalcordRunSettings(runAll = true).effectiveFilter(scenario))
    }
}
