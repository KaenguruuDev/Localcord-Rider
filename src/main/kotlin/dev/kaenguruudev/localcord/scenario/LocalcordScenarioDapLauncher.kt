package dev.kaenguruudev.localcord.scenario

import com.intellij.execution.Executor
import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.kaenguruudev.localcord.run.LocalcordRunConfiguration
import dev.kaenguruudev.localcord.run.LocalcordRunConfigurationMatcher
import dev.kaenguruudev.localcord.run.LocalcordRunConfigurationType

/** Launches the shared Localcord configuration with Rider's debug executor. */
object LocalcordScenarioDapLauncher {
    fun debug(project: Project, scenario: VirtualFile) {
        launch(project, scenario, DefaultDebugExecutor.getDebugExecutorInstance())
    }

    fun launch(project: Project, scenario: VirtualFile, executor: Executor) {
        val runManager = RunManager.getInstance(project)
        val name = "Localcord: ${scenario.nameWithoutExtension}"
        val settings = findExisting(runManager, scenario) ?: create(runManager, name, scenario)

        ProgramRunnerUtil.executeConfiguration(settings, executor)
    }

    private fun findExisting(
        runManager: RunManager,
        scenario: VirtualFile,
    ): RunnerAndConfigurationSettings? =
        runManager.allSettings.firstOrNull { settings ->
            val configuration = settings.configuration as? LocalcordRunConfiguration
            LocalcordRunConfigurationMatcher.matches(configuration, scenario.path, runAll = false)
        }

    private fun create(
        runManager: RunManager,
        name: String,
        scenario: VirtualFile,
    ): RunnerAndConfigurationSettings {
        val configurationType =
            requireNotNull(
                ConfigurationTypeUtil.findConfigurationType(
                    LocalcordRunConfigurationType::class.java
                )
            ) {
                "Localcord run configuration type is not available"
            }
        val factory = configurationType.configurationFactories.first()
        val settings = runManager.createConfiguration(name, factory)
        val configuration = settings.configuration as LocalcordRunConfiguration
        configuration.scenarioPath = scenario.path
        configuration.localcordSettingsChanged()

        runManager.setTemporaryConfiguration(settings)
        return settings
    }
}
