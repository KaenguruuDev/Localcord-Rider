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
import javax.swing.JComboBox
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
    private val reportFormatField = JComboBox(arrayOf("", "console", "junit", "both"))
    private val junitPathField = TextFieldWithBrowseButton()
    private val artifactsPathField = TextFieldWithBrowseButton()
    private val repeatField = JTextField()
    private val seedField = JTextField()
    private val shuffleCheckBox = JCheckBox("Shuffle scenarios")
    private val scenarioTimeoutField = JTextField()
    private val parallelField = JTextField()
    private val failFastCheckBox = JCheckBox("Stop after the first failed scenario")
    private val noComposeCheckBox = JCheckBox("Disable Docker Compose orchestration")
    private val reuseEnvironmentCheckBox = JCheckBox("Reuse the Compose environment after the run")
    private val verboseCheckBox = JCheckBox("Enable verbose output")
    private val diagnosticsLevelField =
        JComboBox(arrayOf("", "off", "failure", "error", "warning", "information"))
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
        junitPathField.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.singleFile(),
        )
        artifactsPathField.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFolderDescriptor(),
        )

        addRow("Scenario:", scenarioField)
        addRow("Runner config:", runnerConfigField)
        addRow("Environment file:", environmentFileField)
        addRow("Base URL:", baseUrlField)
        addRow("Filter:", filterField)
        addRow("Report format:", reportFormatField)
        addRow("JUnit output path:", junitPathField)
        addRow("Artifacts path:", artifactsPathField)
        addRow("Repeat:", repeatField)
        addRow("Seed:", seedField)
        addFullWidth(shuffleCheckBox)
        addRow("Scenario timeout:", scenarioTimeoutField)
        addRow("Parallelism:", parallelField)
        addFullWidth(failFastCheckBox)
        addFullWidth(noComposeCheckBox)
        addFullWidth(reuseEnvironmentCheckBox)
        addFullWidth(verboseCheckBox)
        addRow("Diagnostics level:", diagnosticsLevelField)

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
        reportFormatField.setToolTipText(
            HtmlChunk.text("Optional report override: console, junit, or both")
        )
        junitPathField.textField.setToolTipText(HtmlChunk.text("Optional JUnit XML output path"))
        artifactsPathField.textField.setToolTipText(
            HtmlChunk.text("Optional directory for failure artifact bundles")
        )
        repeatField.setToolTipText(HtmlChunk.text("Optional number of times to repeat the plan"))
        seedField.setToolTipText(HtmlChunk.text("Optional deterministic integer shuffle seed"))
        scenarioTimeoutField.setToolTipText(
            HtmlChunk.text("Optional per-scenario timeout, for example 30s or 2m")
        )
        parallelField.setToolTipText(HtmlChunk.text("Optional maximum scenario worker count"))
        diagnosticsLevelField.setToolTipText(
            HtmlChunk.text(
                "Optional diagnostics level: off, failure, error, warning, or information"
            )
        )
    }

    override fun resetEditorFrom(configuration: T) {
        scenarioField.text = configuration.scenarioPath
        runnerConfigField.text = configuration.runnerConfigPath
        environmentFileField.text = configuration.environmentFilePath
        baseUrlField.text = configuration.baseUrl
        filterField.text = configuration.filter
        reportFormatField.selectedItem = configuration.reportFormat
        junitPathField.text = configuration.junitPath
        artifactsPathField.text = configuration.artifactsPath
        repeatField.text = configuration.repeat
        seedField.text = configuration.seed
        shuffleCheckBox.isSelected = configuration.shuffle
        scenarioTimeoutField.text = configuration.scenarioTimeout
        parallelField.text = configuration.parallel
        failFastCheckBox.isSelected = configuration.failFast
        noComposeCheckBox.isSelected = configuration.noCompose
        reuseEnvironmentCheckBox.isSelected = configuration.reuseEnvironment
        verboseCheckBox.isSelected = configuration.verbose
        diagnosticsLevelField.selectedItem = configuration.diagnosticsLevel
    }

    override fun applyEditorTo(configuration: T) {
        configuration.scenarioPath = scenarioField.text.trim()
        configuration.runnerConfigPath = runnerConfigField.text.trim()
        configuration.environmentFilePath = environmentFileField.text.trim()
        configuration.baseUrl = baseUrlField.text.trim()
        configuration.filter = filterField.text.trim()
        configuration.reportFormat = reportFormatField.selectedItem?.toString().orEmpty()
        configuration.junitPath = junitPathField.text.trim()
        configuration.artifactsPath = artifactsPathField.text.trim()
        configuration.repeat = repeatField.text.trim()
        configuration.seed = seedField.text.trim()
        configuration.shuffle = shuffleCheckBox.isSelected
        configuration.scenarioTimeout = scenarioTimeoutField.text.trim()
        configuration.parallel = parallelField.text.trim()
        configuration.failFast = failFastCheckBox.isSelected
        configuration.noCompose = noComposeCheckBox.isSelected
        configuration.reuseEnvironment = reuseEnvironmentCheckBox.isSelected
        configuration.verbose = verboseCheckBox.isSelected
        configuration.diagnosticsLevel = diagnosticsLevelField.selectedItem?.toString().orEmpty()
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
