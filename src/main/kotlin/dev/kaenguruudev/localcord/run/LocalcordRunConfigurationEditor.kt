package dev.kaenguruudev.localcord.run

import com.intellij.ide.setToolTipText
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

internal class LocalcordRunConfigurationEditor<T>(project: Project) : SettingsEditor<T>()
    where T : LocalcordSettingsHolder {
    private val scenarioField = TextFieldWithBrowseButton()
    private val runnerConfigField = TextFieldWithBrowseButton()
    private val environmentFileField = TextFieldWithBrowseButton()
    private val baseUrlField = JTextField()
    private val filterField = JTextField()
    private val noComposeCheckBox = JCheckBox("Disable Docker Compose orchestration")
    private val verboseCheckBox = JCheckBox("Enable verbose output")
    private val panel = JPanel(GridBagLayout())
    private var row = 0

    init {
        scenarioField.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.singleFile(),
        )
        runnerConfigField.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.singleFile(),
        )
        environmentFileField.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.singleFile(),
        )

        addRow("Scenario:", scenarioField)
        addRow("Runner config:", runnerConfigField)
        addRow("Environment file:", environmentFileField)
        addRow("Base URL:", baseUrlField)
        addRow("Filter:", filterField)
        addFullWidth(noComposeCheckBox)
        addFullWidth(verboseCheckBox)

        scenarioField.textField.setToolTipText(HtmlChunk.text("The .lc scenario file to execute"))
        runnerConfigField.textField.setToolTipText(
            HtmlChunk.text(
                "Optional override. Leave empty to find localcord.runner.yaml in the scenario's parent directories"
            )
        )
        environmentFileField.textField.setToolTipText(
            HtmlChunk.text("Optional dotenv file passed as --env-file")
        )
        baseUrlField.setToolTipText(
            HtmlChunk.text("Optional absolute http:// or https:// Localcord URL")
        )
        filterField.setToolTipText(
            HtmlChunk.text("Optional Localcord filter; empty runs only the selected scenario")
        )
    }

    override fun resetEditorFrom(configuration: T) {
        scenarioField.text = configuration.scenarioPath
        runnerConfigField.text = configuration.runnerConfigPath
        environmentFileField.text = configuration.environmentFilePath
        baseUrlField.text = configuration.baseUrl
        filterField.text = configuration.filter
        noComposeCheckBox.isSelected = configuration.noCompose
        verboseCheckBox.isSelected = configuration.verbose
    }

    override fun applyEditorTo(configuration: T) {
        configuration.scenarioPath = scenarioField.text.trim()
        configuration.runnerConfigPath = runnerConfigField.text.trim()
        configuration.environmentFilePath = environmentFileField.text.trim()
        configuration.baseUrl = baseUrlField.text.trim()
        configuration.filter = filterField.text.trim()
        configuration.noCompose = noComposeCheckBox.isSelected
        configuration.verbose = verboseCheckBox.isSelected
        configuration.localcordSettingsChanged()
    }

    override fun createEditor(): JComponent =
        JPanel(BorderLayout()).also {
            it.add(panel, BorderLayout.NORTH)
        }

    private fun addRow(label: String, component: JComponent) {
        val labelConstraints =
            GridBagConstraints().apply {
                gridx = 0
                gridy = row
                anchor = GridBagConstraints.WEST
                insets = JBUI.insets(0, 0, 6, 8)
            }
        panel.add(JLabel(label), labelConstraints)

        val componentConstraints =
            GridBagConstraints().apply {
                gridx = 1
                gridy = row++
                weightx = 1.0
                fill = GridBagConstraints.HORIZONTAL
                insets = JBUI.insets(0, 0, 6, 0)
            }
        panel.add(component, componentConstraints)
    }

    private fun addFullWidth(component: JComponent) {
        val constraints =
            GridBagConstraints().apply {
                gridx = 0
                gridy = row++
                gridwidth = 2
                anchor = GridBagConstraints.WEST
                fill = GridBagConstraints.HORIZONTAL
                weightx = 1.0
                insets = JBUI.insets(0, 0, 6, 0)
            }
        panel.add(component, constraints)
    }
}
