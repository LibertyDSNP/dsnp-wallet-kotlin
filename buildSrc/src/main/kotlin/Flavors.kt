
sealed class Flavors {
    companion object {
        const val DEV = "dev"
        const val PROD = "prod"
        const val dimension = "version"
    }
    object Dev : Flavors() {
        const val applicationIdSuffix = ".dev"
        const val versionNameSuffix = " dev"
        val WEB_URL = BuildConfigField(
            type = "String",
            key = "WEB_URL",
            value = "\"dev-custodial-wallet.liberti.social\""
        )
        val webUrl = ManifestPlaceHolder(
            key = "WEB_URL",
            value = "\"dev-custodial-wallet.liberti.social\""
        )
    }

    object Prod : Flavors() {
        val WEB_URL = BuildConfigField(
            type = "String",
            key = "WEB_URL",
            value = "\"dev-custodial-wallet.liberti.social\""
        )
        val webUrl = ManifestPlaceHolder(
            key = "WEB_URL",
            value = "\"dev-custodial-wallet.liberti.social\""
        )
    }
}

data class BuildConfigField(
    val type: String,
    val key: String,
    val value: String
)

data class ManifestPlaceHolder(
    val key: String,
    val value: String
)