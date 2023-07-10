package com.unfinished.dsnp_wallet_kotlin.util

sealed class Tag {

    object BottomBar : Tag() {
        const val home = "bottom_home"
        const val settings = "bottom_settings"
    }

    object DebugMenu : Tag() {
        const val title = "txt_title"
        const val navigate = "debug_navigate"
        const val chainTest = "debug_chain_test"
    }

    object DebugToolbar : Tag() {
        const val menu = "btn_debug_menu"
        const val hide = "btn_debug_hide"
    }

    object DebugNavigateScreen : Tag() {
        const val title = "txt_title"
        const val splash = "debug_navigate_splash"
        const val landing = "debug_navigate_landing"
        const val home = "debug_navigate_home"
    }

    object CongratulationsScreen : Tag() {
        const val header = "txt_header"
        const val username = "txt_username"
        const val desc = "txt_desc"
        const val letsGo = "btn_lets_go"
        const val skip = "btn_skip"
    }

    object IdentityScreen : Tag() {
        const val profile = "img_profile"
        const val edit = "btn_edit"
        const val username = "txt_username"
        const val socialProgress = "txt_social_progress"
        const val socialProgressBar = "bar_social_progress"
        const val seeAll = "btn_see_all"
    }

    object CreateIdentityScreen : Tag() {
        const val pullDown = "img_pull_down"
        const val stepTracker = "txt_step_tracker"
        const val title = "txt_title"
        const val header = "txt_header"
        const val claimHandle = "input_claim_handle"
        const val handleRequirements = "txt_handle_requirements"
        const val next = "btn_next"
        const val agree = "btn_agree"
        const val handle = "txt_handle"
        const val numberDesc = "txt_number_desc"
        const val agreeTextBlock = "txt_agree_block"
        const val termsAndPrivacy = "link_terms_privacy"
    }

    object LandingPageScreen : Tag() {
        const val logo = "img_logo"
        const val title = "txt_title"
        const val desc = "txt_desc"
        const val createIdentity = "btn_create_identity"
        const val haveId = "btn_have_id"
        const val restoreAccount = "btn_restore_account"
        const val termsAndPrivacy = "link_terms_privacy"
    }

    object RecoveryPhraseScreen : Tag() {
        const val title = "txt_title"
        const val securityTitle = "txt_security"
        const val securityDesc = "txt_security_desc"
        const val testTitle = "txt_test"
        const val testDesc = "txt_test_desc"

        /**
         * Each item will have a suffix appended at the end. This value will match what is on screen
         * IE: txt_seed_01
         */
        const val seed = "txt_seed"
        const val writtenDown = "btn_written_down"
    }

    object RecoveryTestScreenScreen : Tag() {
        const val title = "txt_title"
        const val header = "txt_header"
        const val desc = "txt_desc"

        /**
         * Each item will have a suffix appended at the end. This value will match what is on screen
         * IE: txt_guess_seed_01
         */
        const val guessSeed = "txt_guess_seed"

        /**
         * Each item will have a suffix appended at the end. This value will match what is on screen
         * IE: txt_guess_seed_01
         */
        const val choiceSeed = "txt_choice_seed"

        const val error = "txt_error"
        const val continueBtn = "btn_continue"
    }

    object SettingsScreen : Tag() {
        const val title = "txt_title"
        const val neverBackup = "txt_never_backup"
        const val recovery_desc = "txt_recovery_desc"
        const val revealRecovery = "btn_reveal_recovery"

        /**
         * The setting tags will both have index as their suffix.
         * IE: txt_setting_title_0 and txt_setting_desc_0
         */
        const val settingTitle = "txt_setting_title"
        const val settingDesc = "txt_setting_desc"

        const val logout = "btn_logout"
    }

    object SocialSetupScreen : Tag() {
        const val title = "txt_title"
        const val socialProgress = "txt_social_progress"
        const val socialProgressBar = "bar_social_progress"
        const val desc = "txt_desc"

        /**
         * Each task button will have an index for their suffix
         * IE: btn_task_0
         */
        const val task = "btn_task"
    }

    object RestoreWalletScreen : Tag() {
        const val logo = "img_logo"
        const val title = "txt_title"
        const val recoveryPhrase = "txt_recovery_phase"
        const val recoveryPhraseDesc = "txt_recovery_phase_desc"
        const val recoveryPhraseError = "txt_recovery_phase_error"
        const val connect = "btn_connect"
        const val cancel = "btn_cancel"
        const val tryAgain = "btn_try_again"
        const val createIdentity = "btn_create_identity"
    }

    object LogoutScreen: Tag() {
        const val logoutIcon = "img_logout"
        const val header = "txt_header"
        const val desc = "txt_desc"
        const val primaryButton = "btn_primary"
        const val secondaryButton = "btn_secondary"
    }

    object AgreeToUseScreen: Tag() {
        const val pullDown = "img_pull_down"
        const val title = "txt_title"
        const val header = "txt_header"
        const val body = "txt_body"
        const val agree = "btn_agree"
        const val bottomText = "txt_bottom_text"
    }
}
