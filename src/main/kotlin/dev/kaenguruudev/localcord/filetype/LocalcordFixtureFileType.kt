package dev.kaenguruudev.localcord.filetype

import com.intellij.openapi.fileTypes.LanguageFileType
import dev.kaenguruudev.localcord.LocalcordIcons
import javax.swing.Icon
import org.jetbrains.yaml.YAMLLanguage

class LocalcordFixtureFileType private constructor() : LanguageFileType(YAMLLanguage.INSTANCE) {
    override fun getName() = "Localcord Fixture"

    override fun getDisplayName() = "Localcord Fixture"

    override fun getDescription() = "Localcord fixture file"

    override fun getDefaultExtension() = "lcf"

    override fun getIcon(): Icon = LocalcordIcons.FIXTURE

    companion object {
        @JvmField val INSTANCE = LocalcordFixtureFileType()
    }
}
