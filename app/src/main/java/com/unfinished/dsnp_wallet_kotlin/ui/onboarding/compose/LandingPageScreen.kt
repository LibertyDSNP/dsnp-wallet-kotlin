package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.compose

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unfinished.dsnp_wallet_kotlin.BuildConfig
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.deeplink.Deeplink
import com.unfinished.dsnp_wallet_kotlin.deeplink.DeeplinkViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.LandingNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.common.bottomsheet.compose.BottomSheet
import com.unfinished.dsnp_wallet_kotlin.ui.common.bottomsheet.viewmodel.BottomSheetViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.common.dialog.viewmodel.DialogViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.RestoreWalletScreenDestination
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.viewmodel.CreateIdentityViewModel
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.components.HyperlinkText
import com.unfinished.uikit.components.LogoLayout
import com.unfinished.uikit.components.PrimaryButton
import com.unfinished.uikit.exts.launchChromeTab
import com.unfinished.uikit.exts.tag

enum class LandingDirection {
    Init, CreateIdentity, HaveId, RestoreAccount
}

@LandingNavGraph(start = true)
@Destination(
    deepLinks = [
        DeepLink(
            uriPattern = Deeplink.JUMP_TO_APP
        ), DeepLink(
            uriPattern = Deeplink.APP_JUMP_TO_APP
        )
    ]
)
@Composable
fun LandingPageScreen(
    navigator: DestinationsNavigator,
    bottomSheetViewModel: BottomSheetViewModel,
    dialogViewModel: DialogViewModel,
    deeplinkViewModel: DeeplinkViewModel,
    createIdentityViewModel: CreateIdentityViewModel
) {
    val bottomSheetUiModelState = bottomSheetViewModel.uiModel.collectAsState()
    val bottomSheetUiModel = bottomSheetUiModelState.value

    val deeplinkState = deeplinkViewModel.deeplinkStateFlow.collectAsState()

    val context = LocalContext.current
    val haveIdLink = stringResource(id = R.string.have_id_link)
    var landingDirection: LandingDirection by remember {
        mutableStateOf(LandingDirection.Init)
    }

    val agreeClick: (LandingDirection) -> Unit = {
        landingDirection = it

        if (bottomSheetUiModel.showAgreeToTerms) {
            bottomSheetViewModel.showAgreeToUse()
        } else {
            bottomSheetViewModel.hide()

            when (landingDirection) {
                LandingDirection.Init -> {}
                LandingDirection.CreateIdentity -> bottomSheetViewModel.showCreateAccount()
                LandingDirection.HaveId -> context.launchChromeTab(
                    haveIdLink,
                    showBackButton = true
                )

                LandingDirection.RestoreAccount -> navigator.navigate(
                    RestoreWalletScreenDestination
                )
            }
        }
    }

    BottomSheet(
        bottomSheetViewModel = bottomSheetViewModel,
        sheetContent = {
            when (it) {
                BottomSheetViewModel.State.Hide -> {}
                BottomSheetViewModel.State.CreateAccount -> CreateIdentityScreen(
                    navigator = navigator,
                    bottomSheetViewModel = bottomSheetViewModel,
                    dialogViewModel = dialogViewModel,
                    createIdentityViewModel = createIdentityViewModel
                )

                BottomSheetViewModel.State.AgreeToUse -> AgreeToUseScreen(
                    agreeClick = {
                        bottomSheetViewModel.agreedToUse()
                    }
                )
            }
        },
        content = {
            LandingPageScreen(
                createIdentityClick = {
                    agreeClick(LandingDirection.CreateIdentity)
                },
                haveIdClick = {
                    agreeClick(LandingDirection.HaveId)
                },
                restoreAccountClick = {
                    agreeClick(LandingDirection.RestoreAccount)
                }
            )
        },
        backPress = {
            if (createIdentityViewModel.previousStep()) bottomSheetViewModel.hide()
        }
    )

    LaunchedEffect(
        key1 = bottomSheetUiModel.showAgreeToTerms,
        block = {
            if (!bottomSheetUiModel.showAgreeToTerms) {
                agreeClick(landingDirection)
            }
        }
    )

    LaunchedEffect(
        key1 = deeplinkState.value,
        block = {
            val deeplink = deeplinkState.value

            if (deeplink is Deeplink.Valid) {
                val path = deeplink.path
                when {
                    path == BuildConfig.DEEP_LINK_JUMP_TO_APP -> Toast.makeText(
                        context, "The app was deep linked", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LandingPageScreen(
    createIdentityClick: () -> Unit,
    haveIdClick: () -> Unit,
    restoreAccountClick: () -> Unit
) {
    LogoLayout(
        modifier = Modifier.padding(horizontal = 36.dp),
        logoTestTag = Tag.LandingPageScreen.logo,
    ) {
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = stringResource(id = R.string.landing_title),
            style = MainTypography.title,
            color = MainColors.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.tag(Tag.LandingPageScreen.title)
        )

        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = stringResource(id = R.string.landing_sub_title),
            style = MainTypography.body,
            color = MainColors.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .tag(Tag.LandingPageScreen.desc),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(56.dp))
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .tag(Tag.LandingPageScreen.createIdentity),
            text = stringResource(id = R.string.landing_create_identity_btn),
            onClick = createIdentityClick
        )

        Spacer(modifier = Modifier.size(32.dp))
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .tag(Tag.LandingPageScreen.haveId),
            text = stringResource(id = R.string.landing_have_identity),
            onClick = haveIdClick
        )

        Spacer(modifier = Modifier.size(16.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.restore_account),
                style = MainTypography.body.copy(),
                color = MainColors.onBackground,
                modifier = Modifier
                    .clickable(onClick = restoreAccountClick)
                    .tag(Tag.LandingPageScreen.restoreAccount)
                    .padding(16.dp)
                    .align(Alignment.Center),
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        TermsAndPrivacy(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .tag(Tag.LandingPageScreen.termsAndPrivacy)
        )

        Spacer(modifier = Modifier.size(32.dp))
    }
}

@Composable
fun TermsAndPrivacy(
    modifier: Modifier = Modifier,
    textColor: Color = MainColors.onBackground,
    textAlign: TextAlign = TextAlign.Center
) {
    val context = LocalContext.current
    val termsLink = stringResource(id = R.string.terms_link)
    val privacyLink = stringResource(id = R.string.privacy_policy_link)
    val terms = stringResource(id = R.string.terms)
    val privacyPolicy = stringResource(id = R.string.privacy_policy)

    HyperlinkText(
        modifier = modifier,
        style = MainTypography.body.copy(
            color = textColor,
            textAlign = textAlign
        ),
        fullText = stringResource(id = R.string.signing_up_terms),
        clickableTexts = listOf(
            terms,
            privacyPolicy
        ),
        onClicked = {
            when (it) {
                terms -> context.launchChromeTab(termsLink)
                privacyPolicy -> context.launchChromeTab(privacyLink)
            }
        }
    )
}

@Preview
@Composable
private fun SampleLandingPageScreen() {
    MainTheme {
        LandingPageScreen(
            createIdentityClick = {},
            haveIdClick = {},
            restoreAccountClick = {}
        )
    }
}