package dev.kaenguruudev.localcord.run

import java.io.File

object LocalcordRunnerConfigLocator {
    private const val CONFIG_NAME = "localcord.runner.yaml"

    fun find(start: File): File? {
        var directory = if (start.isDirectory) start else start.parentFile
        while (directory != null) {
            val candidate = File(directory, CONFIG_NAME)
            if (candidate.isFile) return candidate
            directory = directory.parentFile
        }
        return null
    }
}
