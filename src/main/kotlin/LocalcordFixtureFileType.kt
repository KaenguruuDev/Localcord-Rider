package dev.kaenguruudev.localcord

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class LocalcordFixtureFileType private constructor() : LanguageFileType(LocalcordLanguage) {
    override fun getName() = "Localcord Fixture"
    override fun getDisplayName() = "Localcord Fixture"
    override fun getDescription() = "Localcord fixture file"
    override fun getDefaultExtension() = "lcf"
    override fun getIcon(): Icon = LocalcordIcons.FIXTURE

    companion object {
        @JvmField
        val INSTANCE = LocalcordFixtureFileType()
    }
}