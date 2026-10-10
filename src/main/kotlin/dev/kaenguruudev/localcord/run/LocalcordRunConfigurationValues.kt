package dev.kaenguruudev.localcord.run

import org.jdom.Element

/** Serializable values owned by a Localcord run configuration. */
internal data class LocalcordRunConfigurationValues(
    val scenarioPath: String = "",
    val runnerConfigPath: String = "",
    val environmentFilePath: String = "",
    val baseUrl: String = "",
    val filter: String = "",
    val runAll: Boolean = false,
    val noCompose: Boolean = false,
    val verbose: Boolean = false,
) {
    fun writeTo(element: Element) {
        element.setAttribute(SCENARIO_PATH_ATTRIBUTE, scenarioPath)
        element.setAttribute(RUNNER_CONFIG_PATH_ATTRIBUTE, runnerConfigPath)
        element.setAttribute(ENVIRONMENT_FILE_PATH_ATTRIBUTE, environmentFilePath)
        element.setAttribute(BASE_URL_ATTRIBUTE, baseUrl)
        element.setAttribute(FILTER_ATTRIBUTE, filter)
        element.setAttribute(RUN_ALL_ATTRIBUTE, runAll.toString())
        element.setAttribute(NO_COMPOSE_ATTRIBUTE, noCompose.toString())
        element.setAttribute(VERBOSE_ATTRIBUTE, verbose.toString())
    }

    companion object {
        private const val SCENARIO_PATH_ATTRIBUTE = "scenarioPath"
        private const val RUNNER_CONFIG_PATH_ATTRIBUTE = "runnerConfigPath"
        private const val ENVIRONMENT_FILE_PATH_ATTRIBUTE = "environmentFilePath"
        private const val BASE_URL_ATTRIBUTE = "baseUrl"
        private const val FILTER_ATTRIBUTE = "filter"
        private const val RUN_ALL_ATTRIBUTE = "runAll"
        private const val NO_COMPOSE_ATTRIBUTE = "noCompose"
        private const val VERBOSE_ATTRIBUTE = "verbose"

        fun from(element: Element): LocalcordRunConfigurationValues =
            LocalcordRunConfigurationValues(
                scenarioPath = element.getAttributeValue(SCENARIO_PATH_ATTRIBUTE).orEmpty(),
                runnerConfigPath =
                    element.getAttributeValue(RUNNER_CONFIG_PATH_ATTRIBUTE).orEmpty(),
                environmentFilePath =
                    element.getAttributeValue(ENVIRONMENT_FILE_PATH_ATTRIBUTE).orEmpty(),
                baseUrl = element.getAttributeValue(BASE_URL_ATTRIBUTE).orEmpty(),
                filter = element.getAttributeValue(FILTER_ATTRIBUTE).orEmpty(),
                runAll = element.getAttributeValue(RUN_ALL_ATTRIBUTE)?.toBoolean() ?: false,
                noCompose = element.getAttributeValue(NO_COMPOSE_ATTRIBUTE)?.toBoolean() ?: false,
                verbose = element.getAttributeValue(VERBOSE_ATTRIBUTE)?.toBoolean() ?: false,
            )
    }
}
