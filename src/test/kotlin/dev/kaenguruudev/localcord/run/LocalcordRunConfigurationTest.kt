package dev.kaenguruudev.localcord.run

import com.intellij.execution.configurations.RuntimeConfigurationException
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.mock.MockProject
import com.intellij.openapi.util.Disposer
import java.io.File
import java.nio.file.Files
import org.jdom.Element
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalcordRunConfigurationTest {
    @Test
    fun onlyAdvertisesTheDebugExecutor() {
        withConfiguration { configuration ->
            assertTrue(configuration.canRun("Debug"))
            assertFalse(configuration.canRun(DefaultRunExecutor.EXECUTOR_ID))
            assertFalse(configuration.canRun("Unknown"))
        }
    }

    @Test
    fun nativeProgramRunnerOnlyAcceptsTheRunExecutor() {
        val runner = LocalcordRunProgramRunner()

        withConfiguration { configuration ->
            assertTrue(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, configuration))
            assertFalse(runner.canRun("Debug", configuration))
        }
    }

    @Test
    fun localcordSettingsChangedRefreshesDapConfiguration() {
        withTempDirectory { root ->
            val scenario =
                root.resolve("scenarios/ping.lc").apply {
                    parentFile.mkdirs()
                    writeText("scenario: ping\n")
                }
            val config = root.resolve("localcord.runner.yaml").apply { writeText("version: 1\n") }
            val environment = root.resolve(".env.test").apply { writeText("TOKEN=test\n") }

            withConfiguration { configuration ->
                configuration.scenarioPath = scenario.path
                configuration.runnerConfigPath = config.path
                configuration.environmentFilePath = environment.path
                configuration.baseUrl = "https://localhost:9090"
                configuration.filter = "smoke|ping"
                configuration.noCompose = true
                configuration.verbose = true

                configuration.localcordSettingsChanged()

                assertEquals("localcord dap", configuration.command)
                assertEquals(scenario.path, configuration.file)
                assertEquals(root.path, configuration.workingDirectory)
                assertNotNull(configuration.serverMappings)
                assertEquals(
                    "{\"program\":\"${scenario.path}\",\"config\":\"${config.path}\",\"envFile\":\"${environment.path}\",\"baseUrl\":\"https://localhost:9090\",\"filter\":\"smoke|ping\",\"noCompose\":true,\"verbose\":true}",
                    configuration.launchConfiguration,
                )
            }
        }
    }

    @Test
    fun localcordSettingsChangedResolvesRelativeScenarioPathForDapConfiguration() {
        withTempDirectory { root ->
            val scenario = root.resolve("scenarios/ping.lc").apply {
                parentFile.mkdirs()
                writeText("scenario: ping\n")
            }
            val config = root.resolve("localcord.runner.yaml").apply { writeText("version: 1\n") }
            val relativeScenarioPath =
                File(System.getProperty("user.dir"))
                    .toPath()
                    .toAbsolutePath()
                    .normalize()
                    .relativize(scenario.toPath().toAbsolutePath().normalize())
                    .toString()

            withConfiguration { configuration ->
                configuration.scenarioPath = relativeScenarioPath
                configuration.runnerConfigPath = config.path

                configuration.localcordSettingsChanged()

                assertEquals(
                    "{\"program\":\"${File(relativeScenarioPath).absolutePath}\",\"config\":\"${config.path}\"}",
                    configuration.launchConfiguration,
                )
            }
        }
    }

    @Test
    fun settingsSnapshotContainsAllConfigurationValues() {
        withConfiguration { configuration ->
            configuration.scenarioPath = "/workspace/scenarios/ping.lc"
            configuration.runnerConfigPath = "../localcord.runner.yaml"
            configuration.environmentFilePath = ".env.test"
            configuration.baseUrl = "https://localhost:9090"
            configuration.filter = "smoke|ping"
            configuration.runAll = true
            configuration.noCompose = true
            configuration.verbose = true

            assertEquals(
                LocalcordRunSettings(
                    scenarioPath = "/workspace/scenarios/ping.lc",
                    runnerConfigPath = "../localcord.runner.yaml",
                    environmentFilePath = ".env.test",
                    baseUrl = "https://localhost:9090",
                    filter = "smoke|ping",
                    runAll = true,
                    noCompose = true,
                    verbose = true,
                ),
                configuration.settings(),
            )
        }
    }

    @Test
    fun checkConfigurationRejectsMissingScenario() {
        withConfiguration { configuration ->
            assertConfigurationError(configuration, "A Localcord scenario file is required")
        }
    }

    @Test
    fun checkConfigurationRejectsScenarioThatIsNotAFile() {
        withTempDirectory { root ->
            withConfiguration { configuration ->
                configuration.scenarioPath = root.resolve("missing.lc").path

                assertConfigurationError(
                    configuration,
                    "Scenario file does not exist: ${configuration.scenarioPath}",
                )
            }
        }
    }

    @Test
    fun checkConfigurationRejectsMissingRunnerConfiguration() {
        withTempDirectory { root ->
            val scenario = root.resolve("ping.lc").apply { writeText("scenario: ping\n") }

            withConfiguration { configuration ->
                configuration.scenarioPath = scenario.path

                assertConfigurationError(
                    configuration,
                    "Localcord runner configuration file not found for: ${scenario.path}",
                )
            }
        }
    }

    @Test
    fun checkConfigurationRejectsMissingEnvironmentFile() {
        withTempDirectory { root ->
            val scenario = root.resolve("ping.lc").apply { writeText("scenario: ping\n") }
            val config = root.resolve("localcord.runner.yaml").apply { writeText("version: 1\n") }
            val environment = root.resolve("missing.env")

            withConfiguration { configuration ->
                configuration.scenarioPath = scenario.path
                configuration.runnerConfigPath = config.path
                configuration.environmentFilePath = environment.path

                assertConfigurationError(
                    configuration,
                    "Environment file does not exist: ${environment.path}",
                )
            }
        }
    }

    @Test
    fun checkConfigurationRejectsInvalidBaseUrl() {
        withTempDirectory { root ->
            val scenario = root.resolve("ping.lc").apply { writeText("scenario: ping\n") }
            val config = root.resolve("localcord.runner.yaml").apply { writeText("version: 1\n") }

            withConfiguration { configuration ->
                configuration.scenarioPath = scenario.path
                configuration.runnerConfigPath = config.path
                configuration.baseUrl = "https:///path"

                assertConfigurationError(
                    configuration,
                    "Base URL must be an absolute http:// or https:// URL: https:///path",
                )
            }
        }
    }

    @Test
    fun checkConfigurationAcceptsDiscoveredRunnerConfigurationAndRefreshesDapSettings() {
        withTempDirectory { root ->
            val scenario =
                root.resolve("scenarios/ping.lc").apply {
                    parentFile.mkdirs()
                    writeText("scenario: ping\n")
                }
            val config = root.resolve("localcord.runner.yaml").apply { writeText("version: 1\n") }

            withConfiguration { configuration ->
                configuration.scenarioPath = scenario.path

                configuration.checkConfiguration()

                assertEquals("localcord dap", configuration.command)
                assertEquals(root.path, configuration.workingDirectory)
                assertEquals(
                    "{\"program\":\"${scenario.path}\",\"config\":\"${config.path}\"}",
                    configuration.launchConfiguration,
                )
            }
        }
    }

    @Test
    fun configurationXmlRoundTripPreservesAllValues() {
        val expected =
            LocalcordRunSettings(
                scenarioPath = "/workspace/scenarios/ping.lc",
                runnerConfigPath = "../localcord.runner.yaml",
                environmentFilePath = ".env.test",
                baseUrl = "https://localhost:9090",
                filter = "smoke|ping",
                runAll = true,
                noCompose = true,
                verbose = true,
            )
        val element = Element("configuration")

        withConfiguration { configuration ->
            configuration.scenarioPath = expected.scenarioPath
            configuration.runnerConfigPath = expected.runnerConfigPath
            configuration.environmentFilePath = expected.environmentFilePath
            configuration.baseUrl = expected.baseUrl
            configuration.filter = expected.filter
            configuration.runAll = expected.runAll
            configuration.noCompose = expected.noCompose
            configuration.verbose = expected.verbose
            configuration.writeExternal(element)
        }

        withConfiguration { restored ->
            restored.readExternal(element)

            assertEquals(expected, restored.settings())
        }
    }

    @Test
    fun clonePreservesAllValues() {
        withConfiguration { configuration ->
            configuration.scenarioPath = "/workspace/scenarios/ping.lc"
            configuration.runnerConfigPath = "/workspace/localcord.runner.yaml"
            configuration.environmentFilePath = "/workspace/.env.test"
            configuration.baseUrl = "https://localhost:9090"
            configuration.filter = "smoke|ping"
            configuration.runAll = true
            configuration.noCompose = true
            configuration.verbose = true

            assertEquals(configuration.settings(), configuration.clone().settings())
        }
    }

    private fun assertConfigurationError(
        configuration: LocalcordRunConfiguration,
        message: String,
    ) {
        val exception =
            assertThrows(RuntimeConfigurationException::class.java) {
                configuration.checkConfiguration()
            }

        assertEquals(message, exception.localizedMessage)
    }

    private fun withConfiguration(test: (LocalcordRunConfiguration) -> Unit) {
        val disposable = Disposer.newDisposable()
        val project = MockProject(null, disposable)
        try {
            val factory = LocalcordRunConfigurationType().configurationFactories.first()
            test(factory.createTemplateConfiguration(project) as LocalcordRunConfiguration)
        } finally {
            Disposer.dispose(disposable)
        }
    }

    private fun withTempDirectory(test: (File) -> Unit) {
        val root = Files.createTempDirectory("localcord-rider-config-").toFile()
        try {
            test(root)
        } finally {
            root.deleteRecursively()
        }
    }
}
