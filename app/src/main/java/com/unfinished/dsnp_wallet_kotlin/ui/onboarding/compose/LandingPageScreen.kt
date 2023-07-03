package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.RestoreWalletScreenDestination
import com.unfinished.dsnp_wallet_kotlin.ui.home.viewmmodel.IdentityViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.viewmodel.CreateIdentityViewModel
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.components.BottomSheet
import com.unfinished.uikit.components.HyperlinkText
import com.unfinished.uikit.components.LogoLayout
import com.unfinished.uikit.components.PrimaryButton
import com.unfinished.uikit.exts.launchChromeTab

@LandingNavGraph(start = true)
@Destination
@Composable
fun LandingPageScreen(
    navigator: DestinationsNavigator,
    identityViewModel: IdentityViewModel,
    createIdentityViewModel: CreateIdentityViewModel
) {
    val context = LocalContext.current
    val haveIdLink = stringResource(id = R.string.have_id_link)
    val bottomSheetVisibleStateFlow = createIdentityViewModel.visibleStateFlow.collectAsState()

    val bottomSheetVisibleState = bottomSheetVisibleStateFlow.value

    BottomSheet(
        showBottomSheet = bottomSheetVisibleState == CreateIdentityViewModel.ShowCreateIdentity,
        sheetContent = {
            CreateIdentityScreen(
                navigator = navigator,
                identityViewModel = identityViewModel,
                createIdentityViewModel = createIdentityViewModel
            )
        },
        content = {
            LandingPageScreen(
                createIdentityClick = {
                    createIdentityViewModel.showCreateIdentity()
                },
                haveIdClick = {
                    context.launchChromeTab(haveIdLink, showBackButton = true)
                },
                restoreAccountClick = {
                    navigator.navigate(RestoreWalletScreenDestination)
                }
            )
        },
        backPress = {
            createIdentityViewModel.previousStep()
        },
        onHidden = {
            createIdentityViewModel.hideCreateIdentity()
        }
    )
}

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
            modifier = Modifier.testTag(Tag.LandingPageScreen.title)
        )

        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = stringResource(id = R.string.landing_sub_title),
            style = MainTypography.body,
            color = MainColors.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(Tag.LandingPageScreen.desc),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(56.dp))
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(Tag.LandingPageScreen.createIdentity),
            text = stringResource(id = R.string.landing_create_identity_btn),
            onClick = createIdentityClick
        )

        Spacer(modifier = Modifier.size(32.dp))
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(Tag.LandingPageScreen.haveId),
            text = stringResource(id = R.string.landing_have_identity),
            onClick = haveIdClick
        )

        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = stringResource(id = R.string.restore_account),
            style = MainTypography.body.copy(),
            color = MainColors.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = restoreAccountClick)
                .testTag(Tag.LandingPageScreen.restoreAccount),
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline
        )

        Spacer(modifier = Modifier.weight(1f))
        TermsAndPrivacy(modifier = Modifier.testTag(Tag.LandingPageScreen.termsAndPrivacy))

        Spacer(modifier = Modifier.size(32.dp))
    }
}

@Composable
fun TermsAndPrivacy(
    modifier: Modifier = Modifier,
    textColor: Color = MainColors.onBackground
) {
    val context = LocalContext.current
    val termsLink = stringResource(id = R.string.terms_link)
    val privacyLink = stringResource(id = R.string.privacy_policy_link)
    val terms = stringResource(id = R.string.terms)
    val privacyPolicy = stringResource(id = R.string.privacy_policy)

    HyperlinkText(
        modifier = modifier.padding(horizontal = 24.dp),
        style = MainTypography.body.copy(
            color = textColor,
            textAlign = TextAlign.Center
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