package dev.kaenguruudev.localcord.language

import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.LanguageServerFactory
import com.redhat.devtools.lsp4ij.server.StreamConnectionProvider

class LocalcordLanguageServerFactory : LanguageServerFactory {
    override fun createConnectionProvider(project: Project): StreamConnectionProvider =
        LocalcordLanguageServerConnectionProvider()
}
