package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.components.Bullet
import com.unfinished.uikit.components.PrimaryButton
import com.unfinished.uikit.components.PullDown
import my.nanihadesuka.compose.ColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSelectionMode

@Composable
fun AgreeToUseScreen(
    agreeClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.8F)
            .background(MainColors.bottomSheetBackground)
            .padding(horizontal = 36.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Bottom
    ) {

        Column(
            modifier = Modifier.weight(1F)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                PullDown(
                    modifier = Modifier.testTag(Tag.AgreeToUseScreen.pullDown)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = stringResource(R.string.agree_to_terms_of_use),
                style = MainTypography.bodyLarge,
                color = MainColors.onBottomSheetBackground,
                modifier = Modifier.testTag(Tag.AgreeToUseScreen.title)
            )

            Spacer(modifier = Modifier.size(8.dp))
            ColumnScrollbar(
                state = scrollState,
                selectionMode = ScrollbarSelectionMode.Full,
                padding = 0.dp,
                thumbColor = MainColors.scrollbar,
                thumbSelectedColor = MainColors.scrollbar
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .verticalScroll(state = scrollState)
                ) {
                    Column(
                        modifier = Modifier.testTag(Tag.AgreeToUseScreen.header)
                    ) {
                        Text(
                            text = stringResource(R.string.by_agreeing),
                            style = MainTypography.bodySemiBold,
                            color = MainColors.onEditTextTitle
                        )
                        Bullet(text = stringResource(R.string.update_your_handle_and_profile_information))
                        Bullet(text = stringResource(R.string.update_your_contacts_groups))
                    }


                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = stringResource(R.string.agree_terms_body),
                        style = MainTypography.body,
                        color = MainColors.onBottomSheetBackground,
                        modifier = Modifier.testTag(Tag.AgreeToUseScreen.body)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(8.dp))
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(Tag.AgreeToUseScreen.agree),
            text = stringResource(R.string.agree),
            enabled = !scrollState.canScrollForward,
            onClick = agreeClick
        )

        Spacer(modifier = Modifier.size(22.dp))
        Text(
            text = stringResource(R.string.by_clicking_the_agree_button_you_agree),
            style = MainTypography.bodyMedium,
            color = MainColors.onTaskButton,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(Tag.AgreeToUseScreen.bottomText),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(22.dp))
    }

    /**
     * This allows us to display the scrollbars when the user opens this view
     */
    LaunchedEffect(
        key1 = Unit,
        block = {
            scrollState.animateScrollTo(1)
        }
    )
}

@Preview
@Composable
fun SampleAgreeToUseScreen() {
    MainTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AgreeToUseScreen(
                agreeClick = {}
            )
        }
    }
}