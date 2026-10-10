package dev.kaenguruudev.localcord.run

import java.nio.file.Path

internal object LocalcordRunConfigurationMatcher {
    fun matches(
        configuration: LocalcordRunConfiguration?,
        scenarioPath: String,
        runAll: Boolean,
    ): Boolean =
        configuration?.let {
            normalizePath(it.scenarioPath) == normalizePath(scenarioPath) && it.runAll == runAll
        } == true

    private fun normalizePath(path: String): Path = Path.of(path).toAbsolutePath().normalize()
}
