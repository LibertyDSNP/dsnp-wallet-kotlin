package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.ui.LandingNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraphs
import com.unfinished.dsnp_wallet_kotlin.ui.home.viewmmodel.IdentityViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.uimodel.RestoreWalletUiModel
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.viewmodel.CreateIdentityViewModel
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.dsnp_wallet_kotlin.util.exts.navigateWithNoBackstack
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainShapes
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.UiState
import com.unfinished.uikit.components.BottomSheet
import com.unfinished.uikit.components.Loading
import com.unfinished.uikit.components.LogoLayout
import com.unfinished.uikit.components.OutlinedText
import com.unfinished.uikit.components.PrimaryButton
import com.unfinished.uikit.components.SecondaryButton

@OptIn(ExperimentalComposeUiApi::class)
@LandingNavGraph
@Destination
@Composable
fun RestoreWalletScreen(
    navigator: DestinationsNavigator,
    identityViewModel: IdentityViewModel,
    createIdentityViewModel: CreateIdentityViewModel
) {
    val uiStateFlow = createIdentityViewModel.uiStateFLow.collectAsState()
    val bottomSheetVisibleStateFlow = createIdentityViewModel.visibleStateFlow.collectAsState()

    val bottomSheetVisibleState = bottomSheetVisibleStateFlow.value
    val keyboardController = LocalSoftwareKeyboardController.current

    BottomSheet(showBottomSheet = bottomSheetVisibleState == CreateIdentityViewModel.ShowCreateIdentity,
        sheetContent = {
            CreateIdentityScreen(
                navigator = navigator,
                identityViewModel = identityViewModel,
                createIdentityViewModel = createIdentityViewModel
            )
        },
        content = {
            when (val uiState = uiStateFlow.value) {
                is UiState.DataLoaded -> RestoreWalletScreen(restoreWalletUiModel = uiState.data.restoreWalletUiModel,
                    onRecoveryChange = { createIdentityViewModel.onRecoveryPhraseChange(it) },
                    connectClick = {
                        keyboardController?.hide()
                        createIdentityViewModel.showRecoveryPhraseLoading()
                    },
                    cancelClick = { navigator.popBackStack() },
                    tryAgainClick = { createIdentityViewModel.showRecoveryPhrase() },
                    createIdentityClick = { createIdentityViewModel.showCreateIdentity() })

                is CreateIdentityViewModel.GoToIdentityFromImport -> {
                    navigator.navigateWithNoBackstack(NavGraphs.main)
                }
            }
        },
        backPress = {
            createIdentityViewModel.previousStep()
        },
        onHidden = {
            createIdentityViewModel.hideCreateIdentity()
        })
}

@Composable
fun RestoreWalletScreen(
    restoreWalletUiModel: RestoreWalletUiModel,
    onRecoveryChange: (String) -> Unit,
    connectClick: () -> Unit,
    cancelClick: () -> Unit,
    tryAgainClick: () -> Unit,
    createIdentityClick: () -> Unit
) {
    LogoLayout(
        modifier = Modifier, logoTestTag = Tag.RestoreWalletScreen.logo
    ) {
        Column(
            modifier = Modifier
                .weight(1F)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                text = stringResource(R.string.import_account),
                style = MainTypography.bodyLargeBold,
                color = MainColors.onBackground,
                modifier = Modifier.testTag(Tag.RestoreWalletScreen.title)
            )

            Spacer(modifier = Modifier.size(24.dp))
            if (restoreWalletUiModel.state == RestoreWalletUiModel.State.Error) Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .padding(horizontal = 28.dp)
                    .border(
                        width = 1.dp, shape = MainShapes.input, color = MainColors.error
                    )
                    .background(
                        color = MainColors.import, shape = MainShapes.input
                    )
                    .padding(
                        horizontal = 12.dp, vertical = 32.dp
                    )
                    .testTag(Tag.RestoreWalletScreen.recoveryPhraseError),
                text = stringResource(R.string.invalid_phrase),
                style = MainTypography.stepCounter,
                color = MainColors.onBackground,
                textAlign = TextAlign.Center
            ) else OutlinedText(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .padding(horizontal = 28.dp)
                    .testTag(Tag.RestoreWalletScreen.recoveryPhrase),
                text = restoreWalletUiModel.recoveryPhrase,
                hint = stringResource(R.string.import_hint),
                onTextChange = onRecoveryChange
            )

            Spacer(modifier = Modifier.size(26.dp))
            Text(
                text = stringResource(R.string.enter_your_12_word_recovery_phrase),
                style = MainTypography.body,
                color = MainColors.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(.65F)
                    .testTag(Tag.RestoreWalletScreen.recoveryPhraseDesc)
            )

            Spacer(modifier = Modifier.size(16.dp))
            when (restoreWalletUiModel.state) {
                RestoreWalletUiModel.State.Init -> PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 46.dp)
                        .testTag(Tag.RestoreWalletScreen.connect),
                    text = stringResource(R.string.connect),
                    onClick = connectClick,
                    enabled = restoreWalletUiModel.continueEnabled
                )

                RestoreWalletUiModel.State.Loading -> Loading()
                RestoreWalletUiModel.State.Error -> TryAgainRow(
                    tryAgainClick = tryAgainClick, createIdentityClick = createIdentityClick
                )
            }

            Spacer(modifier = Modifier.size(18.dp))
            Text(
                text = stringResource(R.string.cancel),
                style = MainTypography.body,
                color = MainColors.onBackground,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable(onClick = cancelClick)
                    .testTag(Tag.RestoreWalletScreen.cancel)
            )
        }

        TermsAndPrivacy()
        Spacer(modifier = Modifier.size(32.dp))
    }
}

@Composable
private fun TryAgainRow(
    tryAgainClick: () -> Unit, createIdentityClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PrimaryButton(
            text = stringResource(R.string.try_again),
            modifier = Modifier
                .weight(.55F)
                .testTag(Tag.RestoreWalletScreen.tryAgain),
            onClick = tryAgainClick
        )
        Spacer(modifier = Modifier.size(8.dp))
        SecondaryButton(
            text = stringResource(R.string.create_identity),
            modifier = Modifier
                .weight(.4F)
                .testTag(Tag.RestoreWalletScreen.createIdentity),
            onClick = createIdentityClick
        )
    }
}

private val sampleRestoreWalletUiModel: RestoreWalletUiModel by lazy {
    RestoreWalletUiModel(
        recoveryPhrase = ""
    )
}

@Preview
@Composable
fun SampleRestoreWalletScreen() {
    MainTheme {
        RestoreWalletScreen(restoreWalletUiModel = sampleRestoreWalletUiModel,
            onRecoveryChange = {},
            connectClick = {},
            cancelClick = {},
            tryAgainClick = {},
            createIdentityClick = {})
    }
}


@Preview
@Composable
fun SampleRestoreWalletScreenError() {
    MainTheme {
        RestoreWalletScreen(restoreWalletUiModel = sampleRestoreWalletUiModel.copy(state = RestoreWalletUiModel.State.Error),
            onRecoveryChange = {},
            connectClick = {},
            cancelClick = {},
            tryAgainClick = {},
            createIdentityClick = {})
    }
}


@Preview
@Composable
fun SampleRestoreWalletScreenLoading() {
    MainTheme {
        RestoreWalletScreen(restoreWalletUiModel = sampleRestoreWalletUiModel.copy(
            recoveryPhrase = "some kind of phrase", state = RestoreWalletUiModel.State.Loading
        ),
            onRecoveryChange = {},
            connectClick = {},
            cancelClick = {},
            tryAgainClick = {},
            createIdentityClick = {})
    }
}