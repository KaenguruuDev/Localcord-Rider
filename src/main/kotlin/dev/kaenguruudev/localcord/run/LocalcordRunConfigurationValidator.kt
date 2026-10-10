package dev.kaenguruudev.localcord.run

import java.net.URI

internal object LocalcordRunConfigurationValidator {
    fun isHttpUrl(value: String): Boolean = runCatching {
        val url = URI(value)
        url.isAbsolute &&
            !url.authority.isNullOrBlank() &&
            (url.scheme.equals("http", ignoreCase = true) ||
                url.scheme.equals("https", ignoreCase = true))
    }
        .getOrDefault(false)
}
