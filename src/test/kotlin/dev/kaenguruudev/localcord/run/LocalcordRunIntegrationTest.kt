package dev.kaenguruudev.localcord.run

import java.io.File
import java.nio.file.Files
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class LocalcordRunIntegrationTest {
    @Test
    fun runnerConfigLocatorFindsNearestAncestorConfig() {
        withTempDirectory("localcord-rider-runner-") { root ->
            val scenarioDirectory = root.resolve("localcord/scenarios/nested").apply { mkdirs() }
            val config = root.resolve("localcord.runner.yaml").apply { writeText("version: 1\n") }
            val scenario = scenarioDirectory.resolve("ping.lc")

            assertEquals(config, LocalcordRunnerConfigLocator.find(scenario))
        }
    }

    @Test
    fun runnerConfigLocatorReturnsNullWhenNoAncestorContainsConfig() {
        withTempDirectory("localcord-rider-no-config-") { root ->
            val scenario = root.resolve("ping.lc")

            assertNull(LocalcordRunnerConfigLocator.find(scenario))
        }
    }

    @Test
    fun runCommandTargetsOneScenarioAndPassesDiscoveredConfig() {
        withTempDirectory("localcord-rider-command-") { root ->
            val scenarioDirectory = root.resolve("localcord/scenarios").apply { mkdirs() }
            val config = root.resolve("localcord.runner.yaml").apply { writeText("version: 1\n") }
            val scenario =
                scenarioDirectory.resolve("ping case.lc").apply { writeText("scenario: ping\n") }
            val expectedParameters =
                listOf("run", "--config", config.absolutePath, "--filter", scenario.absolutePath)

            val command =
                LocalcordRunCommandLine.build(LocalcordRunSettings(scenarioPath = scenario.path))

            assertEquals("localcord", command.exePath)
            assertEquals(expectedParameters, command.parametersList.list)
            assertEquals(config.parentFile.path, command.workDirectory?.path)
        }
    }

    @Test
    fun runCommandRejectsScenarioWhenConfigIsNotFound() {
        withTempDirectory("localcord-rider-command-no-config-") { root ->
            val scenario = root.resolve("ping.lc").apply { writeText("scenario: ping\n") }

            val exception =
                assertThrows(IllegalArgumentException::class.java) {
                    LocalcordRunCommandLine.build(
                        LocalcordRunSettings(scenarioPath = scenario.path)
                    )
                }

            assertEquals(
                "Localcord runner configuration file not found for ${scenario.absolutePath}",
                exception.message,
            )
        }
    }

    @Test
    fun runCommandIncludesConfiguredOverrides() {
        withTempDirectory("localcord-rider-overrides-") { root ->
            val scenarioDirectory = root.resolve("scenarios").apply { mkdirs() }
            val config = root.resolve("runner.yaml").apply { writeText("version: 1\n") }
            val environmentFile = root.resolve(".env.test").apply { writeText("TOKEN=test\n") }
            val scenario =
                scenarioDirectory.resolve("ping.lc").apply { writeText("scenario: ping\n") }
            val settings =
                LocalcordRunSettings(
                    scenarioPath = scenario.path,
                    runnerConfigPath = config.path,
                    environmentFilePath = environmentFile.path,
                    baseUrl = "http://localhost:9090",
                    filter = "smoke|ping",
                    noCompose = true,
                    verbose = true,
                )

            val command = LocalcordRunCommandLine.build(settings)

            assertEquals(
                listOf(
                    "run",
                    "--config",
                    config.absolutePath,
                    "--filter",
                    "smoke|ping",
                    "--env-file",
                    environmentFile.absolutePath,
                    "--base-url",
                    "http://localhost:9090",
                    "--no-compose",
                    "--verbose",
                ),
                command.parametersList.list,
            )
        }
    }

    private fun withTempDirectory(prefix: String, test: (File) -> Unit) {
        val root = Files.createTempDirectory(prefix).toFile()
        try {
            test(root)
        } finally {
            root.deleteRecursively()
        }
    }
}
