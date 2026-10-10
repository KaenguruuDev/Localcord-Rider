package dev.kaenguruudev.localcord.scenario

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import dev.kaenguruudev.localcord.run.LocalcordRunConfigurationLauncher

class LocalcordScenarioRunLineMarkerContributor : RunLineMarkerContributor() {
    override fun getInfo(element: PsiElement): Info? {
        if (!LocalcordScenarioDetector.isScenarioDeclarationAnchor(element)) return null

        val actions =
            arrayOf(
                LocalcordScenarioAction(
                    "Run Localcord scenario",
                    AllIcons.Actions.Execute,
                    LocalcordRunConfigurationLauncher::launch,
                ),
                LocalcordScenarioAction(
                    "Debug Localcord scenario",
                    AllIcons.Actions.StartDebugger,
                    LocalcordScenarioDapLauncher::debug,
                ),
                LocalcordScenarioAction(
                    "Run all Localcord scenarios",
                    AllIcons.Actions.Execute,
                    LocalcordRunConfigurationLauncher::launchAll,
                    "Start all scenarios from the Localcord runner configuration",
                ),
            )

        return Info(AllIcons.Actions.Execute, actions)
    }
}
