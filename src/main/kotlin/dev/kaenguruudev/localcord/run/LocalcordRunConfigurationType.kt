package dev.kaenguruudev.localcord.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.dap.configurations.DAPRunConfigurationOptions
import dev.kaenguruudev.localcord.LocalcordIcons

class LocalcordRunConfigurationType :
    ConfigurationTypeBase(
        "LocalcordRunConfiguration",
        "Localcord",
        "Run a Localcord scenario",
        LocalcordIcons.SCENARIO,
    ) {
    init {
        addFactory(
            object : ConfigurationFactory(this) {
                override fun createTemplateConfiguration(project: Project): RunConfiguration =
                    LocalcordRunConfiguration(project, this, "Localcord")

                override fun getId(): String = "Localcord"

                override fun getOptionsClass(): Class<out BaseState> =
                    DAPRunConfigurationOptions::class.java
            }
        )
    }
}
