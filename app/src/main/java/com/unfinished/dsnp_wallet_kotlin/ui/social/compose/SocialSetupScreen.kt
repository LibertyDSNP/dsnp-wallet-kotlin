package com.unfinished.dsnp_wallet_kotlin.ui.social.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.ui.SocialSetupNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.home.compose.SocialProcessBar
import com.unfinished.dsnp_wallet_kotlin.ui.home.uimodel.IdentityTask
import com.unfinished.dsnp_wallet_kotlin.ui.home.uimodel.IdentityUiModel
import com.unfinished.dsnp_wallet_kotlin.ui.home.viewmmodel.IdentityViewModel
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.UiState
import com.unfinished.uikit.components.Checkmark
import com.unfinished.uikit.components.SimpleToolbar

@SocialSetupNavGraph(start = true)
@Destination
@Composable
fun SocialSetupScreen(
    identityViewModel: IdentityViewModel
) {
    val uiStateFlow = identityViewModel.uiStateFLow.collectAsState()

    when (val uiState = uiStateFlow.value) {
        is UiState.DataLoaded -> SocialSetupScreen(identityUiModel = uiState.data)
    }
}

@Composable
fun SocialSetupScreen(
    identityUiModel: IdentityUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background)
    ) {
        SimpleToolbar(
            title = stringResource(R.string.social_identity),
            testTag = Tag.SocialSetupScreen.title
        )
        Spacer(modifier = Modifier.size(34.dp))
        Body(
            identityUiModel = identityUiModel
        )
    }
}

@Composable
private fun Body(
    identityUiModel: IdentityUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 38.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SocialProcessBar(
            currentCount = identityUiModel.currentCount,
            totalCount = identityUiModel.totalCount,
            socialProgressTestTag = Tag.SocialSetupScreen.socialProgress,
            socialProgressBarTestTag = Tag.SocialSetupScreen.socialProgressBar
        )

        Spacer(modifier = Modifier.size(40.dp))
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .testTag(Tag.SocialSetupScreen.desc),
            text = stringResource(R.string.your_real_name),
            color = MainColors.onBackground,
            textAlign = TextAlign.Center,
            style = MainTypography.bodySmall
        )

        Spacer(modifier = Modifier.size(20.dp))
        identityUiModel.identityTasks.forEachIndexed { index, identityTask ->
            TaskButton(
                identityTask = identityTask,
                testTag = "${Tag.SocialSetupScreen.task}_$index"
            )
            Spacer(modifier = Modifier.size(12.dp))
        }
    }
}

@Composable
private fun TaskButton(
    identityTask: IdentityTask,
    testTag: String
) {
    val roundedCorners = RoundedCornerShape(15.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(roundedCorners)
            .background(if (identityTask.isComplete) MainColors.taskButtonDone else MainColors.taskButton)
            .padding(start = 24.dp, end = 10.dp, top = 18.dp, bottom = 18.dp)
            .testTag(testTag)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = stringResource(id = identityTask.title),
            style = MainTypography.largeButtonText,
            color = if (identityTask.isComplete) MainColors.onTaskButtonDone else MainColors.onTaskButton
        )

        if (identityTask.isComplete) Checkmark(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        ) else Text(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(roundedCorners)
                .background(MainColors.toDo)
                .padding(vertical = 4.dp, horizontal = 8.dp),
            text = stringResource(R.string.to_do),
            color = MainColors.onToDo,
            style = MainTypography.bodySemiBold
        )
    }
}

@Preview
@Composable
private fun SampleSocialSetupScreen() {
    MainTheme {
        SocialSetupScreen(
            identityUiModel = IdentityUiModel(
                iconUrl = null, username = "neverendingwinter.23", identityTasks = listOf(
                    IdentityTask(
                        title = R.string.set_avatar
                    ), IdentityTask(
                        title = R.string.backup_seed_phrase
                    ), IdentityTask(
                        title = R.string.choose_a_handle, isComplete = true
                    )
                )
            )
        )
    }
}