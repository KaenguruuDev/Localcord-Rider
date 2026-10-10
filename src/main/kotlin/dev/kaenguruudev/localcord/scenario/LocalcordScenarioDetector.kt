package dev.kaenguruudev.localcord.scenario

import com.intellij.psi.PsiElement
import dev.kaenguruudev.localcord.filetype.LocalcordScenarioFileType

/** Small, document-based checks used by the editor integration. */
object LocalcordScenarioDetector {
    private const val SCENARIO_PREFIX = "scenario:"

    fun startsWithScenarioDeclaration(text: CharSequence): Boolean =
        firstLineScenarioDeclarationEnd(text) != null

    fun isScenarioDeclarationAnchor(element: PsiElement): Boolean {
        val file = element.containingFile ?: return false
        if (file.virtualFile?.fileType !== LocalcordScenarioFileType.INSTANCE) return false

        val lineEnd = firstLineScenarioDeclarationEnd(file.text) ?: return false
        val range = element.textRange
        return range.startOffset < lineEnd && range.endOffset > 0
    }

    private fun firstLineScenarioDeclarationEnd(text: CharSequence): Int? {
        val lineEnd = text.indexOf('\n').let { if (it < 0) text.length else it }
        var start = 0
        while (start < lineEnd && (text[start].isWhitespace() || text[start] == '\uFEFF')) {
            start++
        }

        if (
            start + SCENARIO_PREFIX.length > lineEnd ||
                !text.regionMatches(start, SCENARIO_PREFIX, 0, SCENARIO_PREFIX.length)
        ) {
            return null
        }

        return lineEnd
    }
}
