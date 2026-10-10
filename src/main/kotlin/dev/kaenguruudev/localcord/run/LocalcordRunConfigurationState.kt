package dev.kaenguruudev.localcord.run

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.runners.ExecutionEnvironment

internal class LocalcordRunConfigurationState(
    environment: ExecutionEnvironment,
    private val settings: LocalcordRunSettings,
) : CommandLineState(environment) {
    override fun startProcess(): OSProcessHandler {
        val commandLine = LocalcordRunCommandLine.build(settings)
        return ProcessHandlerFactory.getInstance().createProcessHandler(commandLine)
    }
}
