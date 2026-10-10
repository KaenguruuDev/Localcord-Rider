package dev.kaenguruudev.localcord.run

import org.jdom.Element

/** Serializable values owned by a Localcord run configuration. */
internal data class LocalcordRunConfigurationValues(
    val scenarioPath: String = "",
    val runnerConfigPath: String = "",
    val environmentFilePath: String = "",
    val baseUrl: String = "",
    val filter: String = "",
    val reportFormat: String = "",
    val junitPath: String = "",
    val artifactsPath: String = "",
    val repeat: String = "",
    val seed: String = "",
    val shuffle: Boolean = false,
    val scenarioTimeout: String = "",
    val parallel: String = "",
    val failFast: Boolean = false,
    val runAll: Boolean = false,
    val noCompose: Boolean = false,
    val reuseEnvironment: Boolean = false,
    val verbose: Boolean = false,
    val diagnosticsLevel: String = "",
) {
    fun writeTo(element: Element) {
        element.setAttribute(SCENARIO_PATH_ATTRIBUTE, scenarioPath)
        element.setAttribute(RUNNER_CONFIG_PATH_ATTRIBUTE, runnerConfigPath)
        element.setAttribute(ENVIRONMENT_FILE_PATH_ATTRIBUTE, environmentFilePath)
        element.setAttribute(BASE_URL_ATTRIBUTE, baseUrl)
        element.setAttribute(FILTER_ATTRIBUTE, filter)
        element.setAttribute(REPORT_FORMAT_ATTRIBUTE, reportFormat)
        element.setAttribute(JUNIT_PATH_ATTRIBUTE, junitPath)
        element.setAttribute(ARTIFACTS_PATH_ATTRIBUTE, artifactsPath)
        element.setAttribute(REPEAT_ATTRIBUTE, repeat)
        element.setAttribute(SEED_ATTRIBUTE, seed)
        element.setAttribute(SHUFFLE_ATTRIBUTE, shuffle.toString())
        element.setAttribute(SCENARIO_TIMEOUT_ATTRIBUTE, scenarioTimeout)
        element.setAttribute(PARALLEL_ATTRIBUTE, parallel)
        element.setAttribute(FAIL_FAST_ATTRIBUTE, failFast.toString())
        element.setAttribute(RUN_ALL_ATTRIBUTE, runAll.toString())
        element.setAttribute(NO_COMPOSE_ATTRIBUTE, noCompose.toString())
        element.setAttribute(REUSE_ENVIRONMENT_ATTRIBUTE, reuseEnvironment.toString())
        element.setAttribute(VERBOSE_ATTRIBUTE, verbose.toString())
        element.setAttribute(DIAGNOSTICS_LEVEL_ATTRIBUTE, diagnosticsLevel)
    }

    companion object {
        private const val SCENARIO_PATH_ATTRIBUTE = "scenarioPath"
        private const val RUNNER_CONFIG_PATH_ATTRIBUTE = "runnerConfigPath"
        private const val ENVIRONMENT_FILE_PATH_ATTRIBUTE = "environmentFilePath"
        private const val BASE_URL_ATTRIBUTE = "baseUrl"
        private const val FILTER_ATTRIBUTE = "filter"
        private const val REPORT_FORMAT_ATTRIBUTE = "reportFormat"
        private const val JUNIT_PATH_ATTRIBUTE = "junitPath"
        private const val ARTIFACTS_PATH_ATTRIBUTE = "artifactsPath"
        private const val REPEAT_ATTRIBUTE = "repeat"
        private const val SEED_ATTRIBUTE = "seed"
        private const val SHUFFLE_ATTRIBUTE = "shuffle"
        private const val SCENARIO_TIMEOUT_ATTRIBUTE = "scenarioTimeout"
        private const val PARALLEL_ATTRIBUTE = "parallel"
        private const val FAIL_FAST_ATTRIBUTE = "failFast"
        private const val RUN_ALL_ATTRIBUTE = "runAll"
        private const val NO_COMPOSE_ATTRIBUTE = "noCompose"
        private const val REUSE_ENVIRONMENT_ATTRIBUTE = "reuseEnvironment"
        private const val VERBOSE_ATTRIBUTE = "verbose"
        private const val DIAGNOSTICS_LEVEL_ATTRIBUTE = "diagnosticsLevel"

        fun from(element: Element): LocalcordRunConfigurationValues =
            LocalcordRunConfigurationValues(
                scenarioPath = element.getAttributeValue(SCENARIO_PATH_ATTRIBUTE).orEmpty(),
                runnerConfigPath =
                    element.getAttributeValue(RUNNER_CONFIG_PATH_ATTRIBUTE).orEmpty(),
                environmentFilePath =
                    element.getAttributeValue(ENVIRONMENT_FILE_PATH_ATTRIBUTE).orEmpty(),
                baseUrl = element.getAttributeValue(BASE_URL_ATTRIBUTE).orEmpty(),
                filter = element.getAttributeValue(FILTER_ATTRIBUTE).orEmpty(),
                reportFormat = element.getAttributeValue(REPORT_FORMAT_ATTRIBUTE).orEmpty(),
                junitPath = element.getAttributeValue(JUNIT_PATH_ATTRIBUTE).orEmpty(),
                artifactsPath = element.getAttributeValue(ARTIFACTS_PATH_ATTRIBUTE).orEmpty(),
                repeat = element.getAttributeValue(REPEAT_ATTRIBUTE).orEmpty(),
                seed = element.getAttributeValue(SEED_ATTRIBUTE).orEmpty(),
                shuffle = element.getAttributeValue(SHUFFLE_ATTRIBUTE)?.toBoolean() ?: false,
                scenarioTimeout = element.getAttributeValue(SCENARIO_TIMEOUT_ATTRIBUTE).orEmpty(),
                parallel = element.getAttributeValue(PARALLEL_ATTRIBUTE).orEmpty(),
                failFast = element.getAttributeValue(FAIL_FAST_ATTRIBUTE)?.toBoolean() ?: false,
                runAll = element.getAttributeValue(RUN_ALL_ATTRIBUTE)?.toBoolean() ?: false,
                noCompose = element.getAttributeValue(NO_COMPOSE_ATTRIBUTE)?.toBoolean() ?: false,
                reuseEnvironment =
                    element.getAttributeValue(REUSE_ENVIRONMENT_ATTRIBUTE)?.toBoolean() ?: false,
                verbose = element.getAttributeValue(VERBOSE_ATTRIBUTE)?.toBoolean() ?: false,
                diagnosticsLevel = element.getAttributeValue(DIAGNOSTICS_LEVEL_ATTRIBUTE).orEmpty(),
            )
    }
}
