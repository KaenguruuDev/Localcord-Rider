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
        if (settings.noCompose) {
            parameters += "--no-compose"
        }
        if (settings.verbose) {
            parameters += "--verbose"
        }

        return GeneralCommandLine("localcord")
            .withParameters(parameters)
            .withWorkDirectory(config.parentFile)
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
    }
}
