package dev.kaenguruudev.localcord.run

/** Configuration fields shared by native runs and Localcord-backed debug sessions. */
internal interface LocalcordSettingsHolder {
    var scenarioPath: String
    var runnerConfigPath: String
    var environmentFilePath: String
    var baseUrl: String
    var filter: String
    var runAll: Boolean
    var noCompose: Boolean
    var verbose: Boolean

    fun localcordSettingsChanged() = Unit
}
