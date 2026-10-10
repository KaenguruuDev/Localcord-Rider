package dev.kaenguruudev.localcord.run

import org.jdom.Element
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class LocalcordRunConfigurationValuesTest {
    @Test
    fun roundTripsAllConfigurationValuesThroughXml() {
        val expected =
            LocalcordRunConfigurationValues(
                scenarioPath = "/workspace/scenarios/ping.lc",
                runnerConfigPath = "../localcord.runner.yaml",
                environmentFilePath = ".env.test",
                baseUrl = "https://localhost:9090/api",
                filter = "smoke|ping",
                reportFormat = "both",
                junitPath = "artifacts/results.xml",
                artifactsPath = "artifacts/failures",
                repeat = "2",
                seed = "42",
                shuffle = true,
                scenarioTimeout = "30s",
                parallel = "4",
                failFast = true,
                runAll = true,
                noCompose = true,
                reuseEnvironment = true,
                verbose = true,
                diagnosticsLevel = "warning",
            )
        val element = Element("configuration")

        expected.writeTo(element)

        assertEquals(expected, LocalcordRunConfigurationValues.from(element))
    }

    @Test
    fun missingBooleanAttributesUseBackwardCompatibleDefaults() {
        val element =
            Element("configuration").setAttribute("scenarioPath", "/workspace/scenarios/ping.lc")

        val values = LocalcordRunConfigurationValues.from(element)

        assertFalse(values.shuffle)
        assertFalse(values.failFast)
        assertFalse(values.noCompose)
        assertFalse(values.reuseEnvironment)
        assertFalse(values.verbose)
    }
}
