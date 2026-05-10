pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "Alfarouk Omar Task - Thmanyah"
include(":app")
include(":domain")
include(":core")
include(":core:network")
include(":data")
include(":data:remote")
include(":core:common")
include(":data:repository")
include(":core:fixtures")
include(":core:designsystem")
include(":core:ui")
include(":feature")
include(":feature:home")
include(":feature:search")
include(":feature:setting")
