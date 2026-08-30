package dev.kaenguruudev.localcord

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class LocalcordScenarioFileType private constructor() : LanguageFileType(LocalcordLanguage) {
    override fun getName() = "Localcord Scenario"
    override fun getDisplayName() = "Localcord Scenario"
    override fun getDescription() = "Localcord scenario file"
    override fun getDefaultExtension() = "lc"
    override fun getIcon(): Icon = LocalcordIcons.SCENARIO

    companion object {
        @JvmField
        val INSTANCE = LocalcordScenarioFileType()
    }
}