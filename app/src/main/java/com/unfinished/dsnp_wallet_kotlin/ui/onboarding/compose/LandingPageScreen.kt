package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.components.HyperlinkText
import com.unfinished.uikit.components.Logo
import com.unfinished.uikit.components.SecondaryButton

@Composable
fun LandingPageScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background)
            .padding(horizontal = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(56.dp))
        Logo()

        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = stringResource(id = R.string.landing_title),
            style = MainTypography.title,
            color = MainColors.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = stringResource(id = R.string.landing_sub_title),
            style = MainTypography.body,
            color = MainColors.onBackground,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(56.dp))
        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.landing_create_identity_btn),
            onClick = {
                TODO()
            }
        )

        Spacer(modifier = Modifier.size(32.dp))
        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.landing_have_identity),
            onClick = {
                TODO()
            }
        )

        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = stringResource(id = R.string.restore_account),
            style = MainTypography.body.copy(),
            color = MainColors.onBackground,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline
        )

        Spacer(modifier = Modifier.weight(1f))
        val terms = stringResource(id = R.string.terms)
        val privacyPolicy = stringResource(id = R.string.privacy_policy)
        HyperlinkText(
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MainTypography.body.copy(
                color = MainColors.onBackground,
                textAlign = TextAlign.Center
            ),
            fullText = stringResource(id = R.string.signing_up_terms),
            clickableTexts = listOf(
                terms,
                privacyPolicy
            ),
            onClicked = {
                when (it) {
                    terms -> TODO()
                    privacyPolicy -> TODO()
                }
            }
        )

        Spacer(modifier = Modifier.size(32.dp))
    }
}

@Preview
@Composable
private fun SampleLandingPageScreen() {
    MainTheme {
        LandingPageScreen()
    }
}