package dev.kaenguruudev.localcord.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RuntimeConfigurationException
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.dap.configurations.DAPRunConfiguration
import com.redhat.devtools.lsp4ij.templates.ServerMappingSettings
import dev.kaenguruudev.localcord.filetype.LocalcordScenarioFileType
import dev.kaenguruudev.localcord.scenario.LocalcordScenarioDapLaunchConfiguration
import java.io.File
import org.jdom.Element

class LocalcordRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String,
) : DAPRunConfiguration(project, factory, name), LocalcordSettingsHolder {
    override var scenarioPath: String = ""
    override var runnerConfigPath: String = ""
    override var environmentFilePath: String = ""
    override var baseUrl: String = ""
    override var filter: String = ""
    override var runAll: Boolean = false
    override var noCompose: Boolean = false
    override var verbose: Boolean = false

    override fun canRun(executorId: String): Boolean = executorId == "Debug"

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        if (executor.id == "Debug") {
            applyDapSettings()
            return requireNotNull(super.getState(executor, environment))
        }

        return LocalcordRunConfigurationState(environment, settings())
    }

    override fun getConfigurationEditor(): SettingsEditor<LocalcordRunConfiguration> =
        LocalcordRunConfigurationEditor<LocalcordRunConfiguration>(project)

    override fun localcordSettingsChanged() {
        applyDapSettings()
    }

    private fun applyDapSettings() {
        val scenario = File(scenarioPath).absoluteFile
        val settings = settings()
        val config = settings.runnerConfigFile(scenario)
        val environmentFile = settings.environmentFile(scenario)

        command = "localcord dap"
        file = scenarioPath
        workingDirectory = config?.parentFile?.path ?: scenario.parent ?: "."
        serverMappings =
            listOf(
                ServerMappingSettings.createFileTypeMappingSettings(
                    LocalcordScenarioFileType.INSTANCE.name,
                    "localcord",
                )
            )
        launchConfiguration =
            LocalcordScenarioDapLaunchConfiguration.build(
                scenarioPath = scenario.absolutePath,
                settings = settings,
                config = config,
                environmentFile = environmentFile,
            )
    }

    override fun checkConfiguration() {
        if (scenarioPath.isBlank()) {
            throw RuntimeConfigurationException("A Localcord scenario file is required")
        }

        val scenario = File(scenarioPath)
        if (!scenario.isFile) {
            throw RuntimeConfigurationException("Scenario file does not exist: $scenarioPath")
        }

        val settings = settings()
        val config = settings.runnerConfigFile(scenario)
        if (config == null || !config.isFile) {
            throw RuntimeConfigurationException(
                "Localcord runner configuration file not found for: $scenarioPath"
            )
        }

        val environmentFile = settings.environmentFile(scenario)
        if (environmentFile != null && !environmentFile.isFile) {
            throw RuntimeConfigurationException(
                "Environment file does not exist: ${environmentFile.path}"
            )
        }

        if (baseUrl.isNotBlank() && !LocalcordRunConfigurationValidator.isHttpUrl(baseUrl)) {
            throw RuntimeConfigurationException(
                "Base URL must be an absolute http:// or https:// URL: $baseUrl"
            )
        }

        applyDapSettings()
        super.checkConfiguration()
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        val values = LocalcordRunConfigurationValues.from(element)
        scenarioPath = values.scenarioPath
        runnerConfigPath = values.runnerConfigPath
        environmentFilePath = values.environmentFilePath
        baseUrl = values.baseUrl
        filter = values.filter
        runAll = values.runAll
        noCompose = values.noCompose
        verbose = values.verbose
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        configurationValues().writeTo(element)
    }

    override fun clone(): LocalcordRunConfiguration {
        val copy = super.clone() as LocalcordRunConfiguration
        copy.apply {
            scenarioPath = this@LocalcordRunConfiguration.scenarioPath
            runnerConfigPath = this@LocalcordRunConfiguration.runnerConfigPath
            environmentFilePath = this@LocalcordRunConfiguration.environmentFilePath
            baseUrl = this@LocalcordRunConfiguration.baseUrl
            filter = this@LocalcordRunConfiguration.filter
            runAll = this@LocalcordRunConfiguration.runAll
            noCompose = this@LocalcordRunConfiguration.noCompose
            verbose = this@LocalcordRunConfiguration.verbose
        }
        return copy
    }

    fun settings(): LocalcordRunSettings =
        LocalcordRunSettings(
            scenarioPath = scenarioPath,
            runnerConfigPath = runnerConfigPath,
            environmentFilePath = environmentFilePath,
            baseUrl = baseUrl,
            filter = filter,
            runAll = runAll,
            noCompose = noCompose,
            verbose = verbose,
        )

    private fun configurationValues(): LocalcordRunConfigurationValues =
        LocalcordRunConfigurationValues(
            scenarioPath = scenarioPath,
            runnerConfigPath = runnerConfigPath,
            environmentFilePath = environmentFilePath,
            baseUrl = baseUrl,
            filter = filter,
            runAll = runAll,
            noCompose = noCompose,
            verbose = verbose,
        )
}
