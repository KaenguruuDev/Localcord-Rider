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
                runAll = true,
                noCompose = true,
                verbose = true,
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

        assertFalse(values.noCompose)
        assertFalse(values.verbose)
    }
}
