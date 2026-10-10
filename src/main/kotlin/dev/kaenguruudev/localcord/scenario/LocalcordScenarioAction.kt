package dev.kaenguruudev.localcord.scenario

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.kaenguruudev.localcord.filetype.LocalcordScenarioFileType
import javax.swing.Icon

internal class LocalcordScenarioAction(
    text: String,
    icon: Icon,
    private val launcher: (Project, VirtualFile) -> Unit,
    description: String = "Start the current Localcord scenario",
) : AnAction(text, description, icon) {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(event: AnActionEvent) {
        val scenario = scenarioFile(event)
        event.presentation.isEnabledAndVisible = event.project != null && scenario != null
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val scenario = scenarioFile(event) ?: return
        launcher(project, scenario)
    }

    private fun scenarioFile(event: AnActionEvent): VirtualFile? {
        val file =
            event.getData(CommonDataKeys.VIRTUAL_FILE)
                ?: event.getData(CommonDataKeys.PSI_FILE)?.virtualFile
                ?: event.getData(CommonDataKeys.PSI_ELEMENT)?.containingFile?.virtualFile
        return file?.takeIf { it.fileType === LocalcordScenarioFileType.INSTANCE }
    }
}
