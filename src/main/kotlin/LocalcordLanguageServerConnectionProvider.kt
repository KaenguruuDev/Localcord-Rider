package dev.kaenguruudev.localcord

import com.redhat.devtools.lsp4ij.server.ProcessStreamConnectionProvider

class LocalcordLanguageServerConnectionProvider : ProcessStreamConnectionProvider() {
    init {
        setCommands(listOf("localcord", "lsp"))
    }
}
