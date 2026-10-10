package dev.kaenguruudev.localcord.run

import com.intellij.execution.configurations.GeneralCommandLine

internal object LocalcordRunCommandLine {
    fun build(settings: LocalcordRunSettings): GeneralCommandLine {
        val scenario = settings.scenarioFile()
        val config =
            requireNotNull(settings.runnerConfigFile(scenario)) {
                "Localcord runner configuration file not found for ${scenario.path}"
            }
        val parameters =
            mutableListOf(
                "run",
                "--config",
                config.absolutePath,
            )

        settings
            .effectiveFilter(scenario)
            .takeIf { it.isNotBlank() }
            ?.let {
                parameters += listOf("--filter", it)
            }

        settings.environmentFile(scenario)?.let {
            parameters += listOf("--env-file", it.absolutePath)
        }
        if (settings.baseUrl.isNotBlank()) {
            parameters += listOf("--base-url", settings.baseUrl)
        }
        if (settings.reportFormat.isNotBlank()) {
            parameters += listOf("--report-format", settings.reportFormat)
        }
        if (settings.junitPath.isNotBlank()) {
            parameters += listOf("--junit-path", settings.junitPath)
        }
        if (settings.artifactsPath.isNotBlank()) {
            parameters += listOf("--artifacts-path", settings.artifactsPath)
        }
        if (settings.repeat.isNotBlank()) {
            parameters += listOf("--repeat", settings.repeat)
        }
        if (settings.seed.isNotBlank()) {
            parameters += listOf("--seed", settings.seed)
        }
        if (settings.shuffle) {
            parameters += "--shuffle"
        }
        if (settings.scenarioTimeout.isNotBlank()) {
            parameters += listOf("--scenario-timeout", settings.scenarioTimeout)
        }
        if (settings.parallel.isNotBlank()) {
            parameters += listOf("--parallel", settings.parallel)
        }
        if (settings.failFast) {
            parameters += "--fail-fast"
        }
        if (settings.noCompose) {
            parameters += "--no-compose"
        }
        if (settings.reuseEnvironment) {
            parameters += "--reuse-environment"
        }
        if (settings.verbose) {
            parameters += "--verbose"
        }
        if (settings.diagnosticsLevel.isNotBlank()) {
            parameters += listOf("--diagnostics-level", settings.diagnosticsLevel)
        }

        return GeneralCommandLine("localcord")
            .withParameters(parameters)
            .withWorkDirectory(config.parentFile)
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
    }
}
