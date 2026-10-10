import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform")
    id("com.diffplug.spotless") version "8.10.2"
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktfmt("0.64").kotlinlangStyle()
    }
}

tasks.named("check") {
    dependsOn("spotlessCheck")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

intellijPlatform {
    publishing {
        token = providers.gradleProperty("intellijPlatformPublishingToken")
    }
}

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    testImplementation(libs.junit)

    // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        rider("2026.2")
        bundledPlugin("org.jetbrains.plugins.yaml")
        plugin("com.redhat.devtools.lsp4ij:0.21.0")
        testFramework(TestFrameworkType.Platform)

        // Add plugin dependencies for compilation here, for example:
        // bundledPlugin("com.intellij.java")
    }
}
