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
    override var reportFormat: String = ""
    override var junitPath: String = ""
    override var artifactsPath: String = ""
    override var repeat: String = ""
    override var seed: String = ""
    override var shuffle: Boolean = false
    override var scenarioTimeout: String = ""
    override var parallel: String = ""
    override var failFast: Boolean = false
    override var runAll: Boolean = false
    override var noCompose: Boolean = false
    override var reuseEnvironment: Boolean = false
    override var verbose: Boolean = false
    override var diagnosticsLevel: String = ""

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
        reportFormat = values.reportFormat
        junitPath = values.junitPath
        artifactsPath = values.artifactsPath
        repeat = values.repeat
        seed = values.seed
        shuffle = values.shuffle
        scenarioTimeout = values.scenarioTimeout
        parallel = values.parallel
        failFast = values.failFast
        runAll = values.runAll
        noCompose = values.noCompose
        reuseEnvironment = values.reuseEnvironment
        verbose = values.verbose
        diagnosticsLevel = values.diagnosticsLevel
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
            reportFormat = this@LocalcordRunConfiguration.reportFormat
            junitPath = this@LocalcordRunConfiguration.junitPath
            artifactsPath = this@LocalcordRunConfiguration.artifactsPath
            repeat = this@LocalcordRunConfiguration.repeat
            seed = this@LocalcordRunConfiguration.seed
            shuffle = this@LocalcordRunConfiguration.shuffle
            scenarioTimeout = this@LocalcordRunConfiguration.scenarioTimeout
            parallel = this@LocalcordRunConfiguration.parallel
            failFast = this@LocalcordRunConfiguration.failFast
            runAll = this@LocalcordRunConfiguration.runAll
            noCompose = this@LocalcordRunConfiguration.noCompose
            reuseEnvironment = this@LocalcordRunConfiguration.reuseEnvironment
            verbose = this@LocalcordRunConfiguration.verbose
            diagnosticsLevel = this@LocalcordRunConfiguration.diagnosticsLevel
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
            reportFormat = reportFormat,
            junitPath = junitPath,
            artifactsPath = artifactsPath,
            repeat = repeat,
            seed = seed,
            shuffle = shuffle,
            scenarioTimeout = scenarioTimeout,
            parallel = parallel,
            failFast = failFast,
            runAll = runAll,
            noCompose = noCompose,
            reuseEnvironment = reuseEnvironment,
            verbose = verbose,
            diagnosticsLevel = diagnosticsLevel,
        )

    private fun configurationValues(): LocalcordRunConfigurationValues =
        LocalcordRunConfigurationValues(
            scenarioPath = scenarioPath,
            runnerConfigPath = runnerConfigPath,
            environmentFilePath = environmentFilePath,
            baseUrl = baseUrl,
            filter = filter,
            reportFormat = reportFormat,
            junitPath = junitPath,
            artifactsPath = artifactsPath,
            repeat = repeat,
            seed = seed,
            shuffle = shuffle,
            scenarioTimeout = scenarioTimeout,
            parallel = parallel,
            failFast = failFast,
            runAll = runAll,
            noCompose = noCompose,
            reuseEnvironment = reuseEnvironment,
            verbose = verbose,
            diagnosticsLevel = diagnosticsLevel,
        )
}
