package com.unfinished.dsnp_wallet_kotlin.ui.home.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.ui.BottomBarNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraphs
import com.unfinished.dsnp_wallet_kotlin.ui.home.uimodel.IdentityTask
import com.unfinished.dsnp_wallet_kotlin.ui.home.uimodel.IdentityUiModel
import com.unfinished.dsnp_wallet_kotlin.ui.home.viewmmodel.IdentityViewModel
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.UiState
import com.unfinished.uikit.components.CloseableDialog
import com.unfinished.uikit.components.Edit
import com.unfinished.uikit.components.Handle
import com.unfinished.uikit.components.Overlay
import com.unfinished.uikit.components.Profile
import com.unfinished.uikit.components.RoundedProgressBar
import com.unfinished.uikit.toDataLoaded

@BottomBarNavGraph(start = true)
@Destination
@Composable
fun IdentityScreen(
    navigator: DestinationsNavigator,
    identityViewModel: IdentityViewModel
) {
    val uiState = identityViewModel.uiStateFLow.collectAsState()
    val dialogStateFlow = identityViewModel.dialogStateFlow.collectAsState()

    IdentityScreen(
        uiState = uiState.value,
        dialogState = dialogStateFlow.value,
        editProfileClick = {
            //TODO
        },
        seeAllClick = {
            navigator.navigate(NavGraphs.socialSetup)
        },
        letsGoClick = {
            identityViewModel.hideCreateAccountDialog()
            navigator.navigate(NavGraphs.socialSetup)
        },
        createDialogDismiss = { identityViewModel.hideCreateAccountDialog() }
    )
}

@Composable
fun IdentityScreen(
    uiState: UiState<IdentityUiModel>,
    dialogState: IdentityViewModel.Dialog,
    editProfileClick: () -> Unit,
    seeAllClick: () -> Unit,
    letsGoClick: () -> Unit,
    createDialogDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.background(MainColors.background)
    ) {

        when (uiState) {
            is UiState.DataLoaded -> {
                val identityUiModel = uiState.data

                ProfileRow(
                    iconUrl = identityUiModel.iconUrl,
                    username = identityUiModel.username,
                    editProfileClick = editProfileClick
                )

                Spacer(modifier = Modifier.size(52.dp))
                Box {
                    SocialProcessRow(
                        currentCount = identityUiModel.currentCount,
                        totalCount = identityUiModel.totalCount,
                        seeAllClick = seeAllClick
                    )
                    Overlay(modifier = Modifier.fillMaxSize())
                }

                if (dialogState == IdentityViewModel.Dialog.ShowCreate) CloseableDialog(
                    content = {
                        CongratulationsScreen(
                            username = identityUiModel.username,
                            letsGoClick = letsGoClick,
                            onDismiss = createDialogDismiss
                        )
                    },
                    onDismiss = createDialogDismiss
                )
            }

            is UiState.Loading -> {
                //TODO
            }
        }


    }
}

@Composable
private fun ProfileRow(
    iconUrl: String?,
    username: String,
    editProfileClick: () -> Unit
) {
    val (name, number) = username.split(".")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(222.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier.size(138.dp)
        ) {

            Profile(
                modifier = Modifier.align(Alignment.Center),
                iconUrl = iconUrl
            )

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(MainColors.button)
                    .align(Alignment.BottomEnd)
                    .clickable(onClick = editProfileClick),
                contentAlignment = Alignment.Center
            ) {
                Edit()
            }
        }

        Spacer(modifier = Modifier.size(10.dp))
        Handle(
            handle = name,
            suffix = number
        )

        Spacer(modifier = Modifier.size(12.dp))
    }
}

@Composable
fun SocialProcessBar(
    currentCount: Int,
    totalCount: Int,
) {
    val countDisplay = "$currentCount/$totalCount"
    val progress = currentCount.toFloat() / totalCount.toFloat()

    Row {
        Text(
            text = stringResource(R.string.social_identity_complete),
            style = MainTypography.rowHeader,
            modifier = Modifier.weight(1f),
            color = MainColors.onBackground
        )

        Text(
            text = countDisplay,
            style = MainTypography.rowHeader,
            color = MainColors.onBackground
        )
    }

    Spacer(modifier = Modifier.size(12.dp))
    RoundedProgressBar(progress = progress)
}

@Composable
private fun SocialProcessRow(
    currentCount: Int,
    totalCount: Int,
    seeAllClick: () -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 55.dp),
    ) {
        Spacer(modifier = Modifier.size(32.dp))
        SocialProcessBar(
            currentCount = currentCount,
            totalCount = totalCount
        )

        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.see_all),
            style = MainTypography.textLink,
            color = MainColors.hyperLink,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .align(Alignment.End)
                .clickable(onClick = seeAllClick)
        )
    }
}

@Preview
@Composable
private fun SampleIdentityScreen() {
    MainTheme {
        IdentityScreen(
            uiState = IdentityUiModel(
                iconUrl = null,
                username = "neverendingwinter.23",
                identityTasks = listOf(
                    IdentityTask(
                        title = R.string.set_avatar,
                        isComplete = true
                    ),
                    IdentityTask(
                        title = R.string.choose_a_handle,
                        isComplete = true
                    )
                )
            ).toDataLoaded(),
            dialogState = IdentityViewModel.Dialog.Init,
            editProfileClick = {},
            seeAllClick = {},
            letsGoClick = {},
            createDialogDismiss = {}
        )
    }
}