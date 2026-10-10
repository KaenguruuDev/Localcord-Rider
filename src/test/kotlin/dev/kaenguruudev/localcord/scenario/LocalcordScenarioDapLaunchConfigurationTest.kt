package dev.kaenguruudev.localcord.scenario

import dev.kaenguruudev.localcord.run.LocalcordRunSettings
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalcordScenarioDapLaunchConfigurationTest {
    @Test
    fun includesAllConfiguredRunnerSettings() {
        val json =
            LocalcordScenarioDapLaunchConfiguration.build(
                scenarioPath = "/workspace/scenarios/ping.lc",
                settings =
                    LocalcordRunSettings(
                        baseUrl = "https://localhost:9090",
                        filter = "smoke|ping",
                        noCompose = true,
                        verbose = true,
                    ),
                config = File("/workspace/localcord.runner.yaml"),
                environmentFile = File("/workspace/.env.test"),
            )

        val expectedFields =
            listOf(
                "\"program\":\"/workspace/scenarios/ping.lc\"",
                "\"config\":\"/workspace/localcord.runner.yaml\"",
                "\"envFile\":\"/workspace/.env.test\"",
                "\"baseUrl\":\"https://localhost:9090\"",
                "\"filter\":\"smoke|ping\"",
                "\"noCompose\":true",
                "\"verbose\":true",
            )
        val expected = expectedFields.joinToString(separator = ",", prefix = "{", postfix = "}")

        assertEquals(expected, json)
    }

    @Test
    fun escapesJsonStringValuesAndOmitsUnsetOptionalValues() {
        val json =
            LocalcordScenarioDapLaunchConfiguration.build(
                scenarioPath = "/workspace/a\\b\"c\n.lc",
                settings = LocalcordRunSettings(),
                config = null,
                environmentFile = null,
            )

        assertEquals("{\"program\":\"/workspace/a\\\\b\\\"c\\n.lc\"}", json)
    }
}
