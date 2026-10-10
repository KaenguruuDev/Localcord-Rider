package dev.kaenguruudev.localcord.run

/** Configuration fields shared by native runs and Localcord-backed debug sessions. */
internal interface LocalcordSettingsHolder {
    var scenarioPath: String
    var runnerConfigPath: String
    var environmentFilePath: String
    var baseUrl: String
    var filter: String
    var reportFormat: String
    var junitPath: String
    var artifactsPath: String
    var repeat: String
    var seed: String
    var shuffle: Boolean
    var scenarioTimeout: String
    var parallel: String
    var failFast: Boolean
    var runAll: Boolean
    var noCompose: Boolean
    var reuseEnvironment: Boolean
    var verbose: Boolean
    var diagnosticsLevel: String

    fun localcordSettingsChanged() = Unit
}
