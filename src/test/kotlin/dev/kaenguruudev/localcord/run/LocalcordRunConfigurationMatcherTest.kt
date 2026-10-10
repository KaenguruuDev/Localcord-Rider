package dev.kaenguruudev.localcord.run

import com.intellij.mock.MockProject
import com.intellij.openapi.util.Disposer
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Path

class LocalcordRunConfigurationMatcherTest {
    @Test
    fun matchesScenarioAndRunAllMode() {
        withConfiguration { configuration ->
            configuration.scenarioPath = "/workspace/scenarios/ping.lc"

            assertTrue(
                LocalcordRunConfigurationMatcher.matches(
                    configuration,
                    "/workspace/scenarios/ping.lc",
                    runAll = false,
                )
            )
        }
    }

    @Test
    fun matchesRelativeAndNonNormalizedScenarioPath() {
        withConfiguration { configuration ->
            val savedScenarioPath = Path.of("scenarios", "..", "scenarios", "ping.lc")
            configuration.scenarioPath = savedScenarioPath.toString()

            assertTrue(
                LocalcordRunConfigurationMatcher.matches(
                    configuration,
                    savedScenarioPath.toAbsolutePath().normalize().toString(),
                    runAll = false,
                )
            )
        }
    }

    @Test
    fun ignoresConfigurationNameAndMatchesAnyReusableConfiguration() {
        withConfiguration { configuration ->
            configuration.name = "My reusable Localcord configuration"
            configuration.scenarioPath = "/workspace/scenarios/ping.lc"

            assertTrue(
                LocalcordRunConfigurationMatcher.matches(
                    configuration,
                    "/workspace/scenarios/ping.lc",
                    runAll = false,
                )
            )
        }
    }

    @Test
    fun rejectsDifferentScenariosAndRunAllModes() {
        withConfiguration { configuration ->
            configuration.scenarioPath = "/workspace/scenarios/ping.lc"

            assertFalse(
                LocalcordRunConfigurationMatcher.matches(
                    configuration,
                    "/workspace/scenarios/other.lc",
                    runAll = false,
                )
            )
            assertFalse(
                LocalcordRunConfigurationMatcher.matches(
                    configuration,
                    "/workspace/scenarios/ping.lc",
                    runAll = true,
                )
            )
        }
    }

    @Test
    fun matchesAllScenariosConfigurationWhenRequested() {
        withConfiguration { configuration ->
            configuration.scenarioPath = "/workspace/scenarios/ping.lc"
            configuration.runAll = true

            assertTrue(
                LocalcordRunConfigurationMatcher.matches(
                    configuration,
                    "/workspace/scenarios/ping.lc",
                    runAll = true,
                )
            )
        }
    }

    @Test
    fun rejectsNonLocalcordConfigurations() {
        assertFalse(
            LocalcordRunConfigurationMatcher.matches(
                configuration = null,
                scenarioPath = "/workspace/scenarios/ping.lc",
                runAll = false,
            )
        )
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
}
