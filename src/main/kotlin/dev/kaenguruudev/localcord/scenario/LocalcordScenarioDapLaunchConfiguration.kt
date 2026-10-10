package dev.kaenguruudev.localcord.scenario

import dev.kaenguruudev.localcord.run.LocalcordRunSettings
import java.io.File

internal object LocalcordScenarioDapLaunchConfiguration {
    fun build(
        scenarioPath: String,
        settings: LocalcordRunSettings,
        config: File?,
        environmentFile: File?,
    ): String = buildString {
        append('{')
        append("\"program\":").append(jsonString(scenarioPath))
        config?.let { append(",\"config\":").append(jsonString(it.absolutePath)) }
        environmentFile?.let {
            append(",\"envFile\":").append(jsonString(it.absolutePath))
        }
        if (settings.baseUrl.isNotBlank()) {
            append(",\"baseUrl\":").append(jsonString(settings.baseUrl))
        }
        if (settings.filter.isNotBlank()) {
            append(",\"filter\":").append(jsonString(settings.filter))
        }
        if (settings.noCompose) append(",\"noCompose\":true")
        if (settings.verbose) append(",\"verbose\":true")
        append('}')
    }

    private fun jsonString(value: String): String =
        buildString(value.length + 2) {
            append('"')
            value.forEach { character ->
                when (character) {
                    '\\' -> append("\\\\")
                    '"' -> append("\\\"")
                    '\b' -> append("\\b")
                    '\u000C' -> append("\\f")
                    '\n' -> append("\\n")
                    '\r' -> append("\\r")
                    '\t' -> append("\\t")
                    else ->
                        if (character.code < 0x20) {
                            append("\\u%04x".format(character.code))
                        } else {
                            append(character)
                        }
                }
            }
            append('"')
        }
}
