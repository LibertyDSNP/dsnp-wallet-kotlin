
object Config {
    const val BUILD_TOOLS_VERSION = "30.0.3"
    const val NDK_VERSION = "25.1.8937393"

    const val COMPILE_SDK = 33
    const val MINIMUM_SDK = 21
    const val TARGET_SDK = 33

    const val APPLICATION_ID = "com.unfinished.dsnp_wallet_kotlin"
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0"

    val DEEP_LINK_JUMP_TO_APP = BuildConfigField(
        type = "String",
        key = "DEEP_LINK_JUMP_TO_APP",
        value = "\"/jumpIntoTheApp\""
    )
    val deepLinkJumpToApp = ManifestPlaceHolder(
        key = "WEB_URL",
        value = "\"/jumpIntoTheApp\""
    )
}

