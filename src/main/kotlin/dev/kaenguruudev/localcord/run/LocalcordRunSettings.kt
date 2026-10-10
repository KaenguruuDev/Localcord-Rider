package dev.kaenguruudev.localcord.run

import java.io.File

/** Settings shared by the native Localcord runner and the generated DAP launch. */
data class LocalcordRunSettings(
    var scenarioPath: String = "",
    var runnerConfigPath: String = "",
    var environmentFilePath: String = "",
    var baseUrl: String = "",
    var filter: String = "",
    var reportFormat: String = "",
    var junitPath: String = "",
    var artifactsPath: String = "",
    var repeat: String = "",
    var seed: String = "",
    var shuffle: Boolean = false,
    var scenarioTimeout: String = "",
    var parallel: String = "",
    var failFast: Boolean = false,
    var runAll: Boolean = false,
    var noCompose: Boolean = false,
    var reuseEnvironment: Boolean = false,
    var verbose: Boolean = false,
    var diagnosticsLevel: String = "",
) {
    fun scenarioFile(): File = File(scenarioPath).absoluteFile

    fun runnerConfigFile(scenario: File): File? {
        if (runnerConfigPath.isNotBlank()) {
            return resolvePath(runnerConfigPath, scenario.parentFile)
        }

        return LocalcordRunnerConfigLocator.find(scenario)
    }

    fun environmentFile(scenario: File): File? =
        environmentFilePath.takeIf { it.isNotBlank() }?.let { resolvePath(it, scenario.parentFile) }

    fun effectiveFilter(scenario: File): String =
        if (runAll) "" else filter.ifBlank { scenario.absolutePath }

    private fun resolvePath(path: String, baseDirectory: File?): File {
        val file = File(path)
        return if (file.isAbsolute || baseDirectory == null) file.absoluteFile
        else
            File(
                    baseDirectory,
                    path,
                )
                .absoluteFile
    }
}
