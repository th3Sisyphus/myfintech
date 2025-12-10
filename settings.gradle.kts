pluginManagement {
    repositories {
        google()            // Required for Android Gradle plugin and ML Kit
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()            // Required for ML Kit
        mavenCentral()
    }
}

rootProject.name = "MyFintech"
include(":app")
