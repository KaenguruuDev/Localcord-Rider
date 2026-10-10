package dev.kaenguruudev.localcord.run

import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.GenericProgramRunner

/** Runs the shared Localcord configuration with its native command-line state. */
internal class LocalcordRunProgramRunner : GenericProgramRunner<RunnerSettings>() {
    override fun getRunnerId(): String = "LocalcordRunProgramRunner"

    override fun canRun(executorId: String, profile: RunProfile): Boolean =
        executorId == DefaultRunExecutor.EXECUTOR_ID && profile is LocalcordRunConfiguration
}
