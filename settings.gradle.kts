dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://plugins.gradle.org/m2/")
        flatDir {
            dirs("$rootDir/common/libs")
        }
    }
}
rootProject.name = "dsnp-wallet-kotlin"
include(":app")
include(":common")
include(":features:account")
include(":uikit")
include(":data")
