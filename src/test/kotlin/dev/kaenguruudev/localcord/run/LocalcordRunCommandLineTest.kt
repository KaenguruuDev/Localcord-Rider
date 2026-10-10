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
}
