package com.unfinished.dsnp_wallet_kotlin.ui.setting.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.ui.BottomBarNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.LandingPageScreenDestination
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.RecoveryPhraseScreenDestination
import com.unfinished.dsnp_wallet_kotlin.ui.home.compose.RootNavigator
import com.unfinished.dsnp_wallet_kotlin.ui.setting.uimodel.Setting
import com.unfinished.dsnp_wallet_kotlin.ui.setting.uimodel.SettingsUiModel
import com.unfinished.dsnp_wallet_kotlin.ui.setting.viewmodel.SettingsViewModel
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.dsnp_wallet_kotlin.util.exts.navigateWithNoBackstack
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.UiState
import com.unfinished.uikit.components.ArrowRight
import com.unfinished.uikit.components.CloseableDialog
import com.unfinished.uikit.components.LogOut
import com.unfinished.uikit.components.PrimaryButton
import com.unfinished.uikit.components.PrimaryToggle
import com.unfinished.uikit.components.SimpleToolbar
import com.unfinished.uikit.components.SuccessSnackbar
import com.unfinished.uikit.exts.tag

@BottomBarNavGraph
@Destination
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator,
    rootNavigator: RootNavigator,
    resultRecipient: ResultRecipient<RecoveryPhraseScreenDestination, Boolean>,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uiStateFlow = settingsViewModel.uiStateFLow.collectAsState()
    val logoutDialogStateFlow = settingsViewModel.logoutDialogStateFlow.collectAsState()

    when (val uiState = uiStateFlow.value) {
        is UiState.DataLoaded -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MainColors.background)
        ) {
            SettingsScreen(
                settingsUiModel = uiState.data,
                settingClick = {},
                logOutClick = { settingsViewModel.showLogoutDialog() },
                recoveryPhraseClick = {
                    navigator.navigate(RecoveryPhraseScreenDestination)
                }
            )

            SuccessSnackbar(
                text = stringResource(R.string.congratulations),
                showSnackbar = uiState.data.showSnackbar,
                modifier = Modifier.align(Alignment.BottomCenter),
                onDismiss = { settingsViewModel.hideSnackbar() },
                onShown = { settingsViewModel.hideSnackbar() }
            )
        }
    }

    if (logoutDialogStateFlow.value == SettingsViewModel.Logout.Show) CloseableDialog(
        content = {
            LogoutScreen(
                logoutClick = {
                    settingsViewModel.logout()
                    rootNavigator.navigator.navigateWithNoBackstack(LandingPageScreenDestination)
                },
                viewRecoveryPhraseClick = {
                    settingsViewModel.hideLogoutDialog()
                    navigator.navigate(RecoveryPhraseScreenDestination)
                }
            )
        },
        onDismiss = { settingsViewModel.hideLogoutDialog() }
    )

    resultRecipient.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> {}
            is NavResult.Value -> settingsViewModel.showSnackbar()
        }
    }
}

@Composable
fun SettingsScreen(
    settingsUiModel: SettingsUiModel,
    settingClick: (Setting) -> Unit,
    logOutClick: () -> Unit,
    recoveryPhraseClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background)
    ) {
        SimpleToolbar(
            title = stringResource(id = R.string.settings),
            showNav = false,
            testTag = Tag.SettingsScreen.title
        )

        Spacer(modifier = Modifier.size(16.dp))
        Body(
            settingsUiModel = settingsUiModel,
            settingClick = settingClick,
            logOutClick = logOutClick,
            recoveryPhraseClick = recoveryPhraseClick
        )
    }
}

@Composable
private fun Body(
    settingsUiModel: SettingsUiModel,
    settingClick: (Setting) -> Unit,
    logOutClick: () -> Unit,
    recoveryPhraseClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        RecoveryRow(
            recoveryPhraseClick = recoveryPhraseClick
        )

        Spacer(modifier = Modifier.size(41.dp))
        SettingsRow(
            settings = settingsUiModel.settings,
            settingClick = settingClick
        )

        Divider(
            modifier = Modifier.padding(horizontal = 13.dp),
            color = MainColors.divider.copy(alpha = .2F)
        )

        Spacer(modifier = Modifier.size(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 29.dp, end = 33.dp)
                .clickable(onClick = logOutClick)
                .tag(Tag.SettingsScreen.logout)
        ) {
            Text(
                text = stringResource(id = com.unfinished.uikit.R.string.log_out),
                modifier = Modifier.align(Alignment.CenterStart),
                color = MainColors.onBackground,
                style = MainTypography.bodyMediumBold
            )
            LogOut(
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Spacer(modifier = Modifier.size(20.dp))

        Divider(
            modifier = Modifier.padding(horizontal = 13.dp),
            color = MainColors.divider.copy(alpha = .2F)
        )
    }
}

@Composable
private fun RecoveryRow(
    recoveryPhraseClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 29.dp)
    ) {
        Text(
            text = stringResource(R.string.you_have_never_backed_up),
            color = MainColors.onBackground,
            style = MainTypography.body,
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .tag(Tag.SettingsScreen.neverBackup)
        )

        Spacer(modifier = Modifier.size(14.dp))
        Text(
            text = stringResource(R.string.recovery_phrase_is_very_important),
            color = MainColors.onBackground,
            style = MainTypography.body,
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .tag(Tag.SettingsScreen.recovery_desc)
        )

        Spacer(modifier = Modifier.size(16.dp))
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .tag(Tag.SettingsScreen.revealRecovery),
            text = stringResource(R.string.reveal_recovery_phrase),
            onClick = recoveryPhraseClick
        )
    }
}

@Composable
private fun SettingsRow(
    settings: List<Setting>,
    settingClick: (Setting) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 29.dp, end = 33.dp)
    ) {
        settings.forEachIndexed { index, setting ->

            Row(
                modifier = Modifier.clickable(onClick = { settingClick(setting) }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1F)
                ) {
                    Text(
                        text = stringResource(id = setting.title),
                        color = MainColors.onBackground,
                        style = MainTypography.bodyMediumBold,
                        modifier = Modifier.tag("${Tag.SettingsScreen.settingTitle}_$index")
                    )

                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        text = stringResource(id = setting.desc),
                        color = MainColors.onBackground,
                        style = MainTypography.body,
                        modifier = Modifier.tag("${Tag.SettingsScreen.settingDesc}_$index")
                    )
                }

                when (setting) {
                    is Setting.FaceId -> PrimaryToggle(isChecked = setting.isToggled)
                    Setting.Password -> ArrowRight()
                    Setting.Security -> {}
                }
            }

            when (index) {
                settings.lastIndex -> Spacer(modifier = Modifier.size(22.dp))
                else -> Spacer(modifier = Modifier.size(34.dp))
            }
        }
    }
}

@Preview
@Composable
private fun SampleSettingsScreen() {
    MainTheme {
        SettingsScreen(
            settingsUiModel = SettingsUiModel(
                settings = listOf(
                    Setting.Security, Setting.FaceId(), Setting.Password
                )
            ),
            settingClick = {},
            logOutClick = {},
            recoveryPhraseClick = {}
        )
    }
}