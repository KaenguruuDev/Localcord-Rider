package dev.kaenguruudev.localcord.run

import org.junit.Assert.assertEquals
import org.junit.Test

class LocalcordRunCommandLineTest {
    @Test
    fun omitsOptionalSwitchesWhenTheyAreNotConfigured() {
        val command =
            LocalcordRunCommandLine.build(
                LocalcordRunSettings(
                    scenarioPath = "/workspace/scenarios/ping.lc",
                    runnerConfigPath = "/workspace/localcord.runner.yaml",
                )
            )

        assertEquals(
            listOf(
                "run",
                "--config",
                "/workspace/localcord.runner.yaml",
                "--filter",
                "/workspace/scenarios/ping.lc",
            ),
            command.parametersList.list,
        )
    }

    @Test
    fun omitsFilterWhenRunningAllScenarios() {
        val command =
            LocalcordRunCommandLine.build(
                LocalcordRunSettings(
                    scenarioPath = "/workspace/scenarios/ping.lc",
                    runnerConfigPath = "/workspace/localcord.runner.yaml",
                    runAll = true,
                )
            )

        assertEquals(
            listOf("run", "--config", "/workspace/localcord.runner.yaml"),
            command.parametersList.list,
        )
    }

    @Test
    fun includesAllConfiguredLocalcordRunOptions() {
        val command =
            LocalcordRunCommandLine.build(
                LocalcordRunSettings(
                    scenarioPath = "/workspace/scenarios/ping.lc",
                    runnerConfigPath = "/workspace/localcord.runner.yaml",
                    reportFormat = "both",
                    junitPath = "artifacts/results.xml",
                    artifactsPath = "artifacts/failures",
                    repeat = "2",
                    seed = "42",
                    shuffle = true,
                    scenarioTimeout = "30s",
                    parallel = "4",
                    failFast = true,
                    noCompose = true,
                    reuseEnvironment = true,
                    verbose = true,
                    diagnosticsLevel = "warning",
                )
            )

        assertEquals(
            listOf(
                "run",
                "--config",
                "/workspace/localcord.runner.yaml",
                "--filter",
                "/workspace/scenarios/ping.lc",
                "--report-format",
                "both",
                "--junit-path",
                "artifacts/results.xml",
                "--artifacts-path",
                "artifacts/failures",
                "--repeat",
                "2",
                "--seed",
                "42",
                "--shuffle",
                "--scenario-timeout",
                "30s",
                "--parallel",
                "4",
                "--fail-fast",
                "--no-compose",
                "--reuse-environment",
                "--verbose",
                "--diagnostics-level",
                "warning",
            ),
            command.parametersList.list,
        )
    }
}
