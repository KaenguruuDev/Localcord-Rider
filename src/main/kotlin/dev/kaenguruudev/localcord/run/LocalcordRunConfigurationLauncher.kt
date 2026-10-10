package dev.kaenguruudev.localcord.run

import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

object LocalcordRunConfigurationLauncher {
    fun launch(project: Project, scenario: VirtualFile) {
        launch(project, scenario, runAll = false)
    }

    fun launchAll(project: Project, scenario: VirtualFile) {
        launch(project, scenario, runAll = true)
    }

    private fun launch(project: Project, scenario: VirtualFile, runAll: Boolean) {
        val runManager = RunManager.getInstance(project)
        val name =
            if (runAll) "Localcord: All scenarios"
            else "Localcord: ${scenario.nameWithoutExtension}"
        val settings =
            findExisting(runManager, scenario, runAll) ?: create(runManager, name, scenario, runAll)

        ProgramRunnerUtil.executeConfiguration(
            settings,
            DefaultRunExecutor.getRunExecutorInstance(),
        )
    }

    private fun findExisting(
        runManager: RunManager,
        scenario: VirtualFile,
        runAll: Boolean,
    ): RunnerAndConfigurationSettings? =
        runManager.allSettings.firstOrNull { settings ->
            val configuration = settings.configuration as? LocalcordRunConfiguration
            LocalcordRunConfigurationMatcher.matches(configuration, scenario.path, runAll)
        }

    private fun create(
        runManager: RunManager,
        name: String,
        scenario: VirtualFile,
        runAll: Boolean,
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
        configuration.runAll = runAll
        runManager.setTemporaryConfiguration(settings)
        return settings
    }
}
