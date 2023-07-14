
sealed class BuildTypes {
    companion object {
        const val RELEASE = "release"
        const val DEBUG = "debug"
    }
    object Debug : BuildTypes() {
        const val isMinifyEnabled = false
        const val isShrinkResources = false
        const val isDebuggable = true
    }

    object Release : BuildTypes() {
        const val isMinifyEnabled = true
        const val isShrinkResources = true
        const val isDebuggable = false
    }
}