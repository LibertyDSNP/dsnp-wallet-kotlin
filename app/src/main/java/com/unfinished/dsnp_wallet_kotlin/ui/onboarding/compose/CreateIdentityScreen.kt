package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.MainScreenDestination
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.uimodel.CreateIdentityUiModel
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.viewmodel.CreateIdentityViewModel
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.UiState
import com.unfinished.uikit.components.Bullet
import com.unfinished.uikit.components.Handle
import com.unfinished.uikit.components.InputTextField
import com.unfinished.uikit.components.PrimaryButton
import com.unfinished.uikit.components.PullDown
import kotlinx.coroutines.delay

@Composable
fun CreateIdentityScreen(
    navigator: DestinationsNavigator,
    createIdentityViewModel: CreateIdentityViewModel = hiltViewModel()
) {
    val uiState = createIdentityViewModel.uiStateFLow.collectAsState()
    val bottomSheetVisibleStateFlow = createIdentityViewModel.visibleStateFlow.collectAsState()

    val bottomSheetVisibleState = bottomSheetVisibleStateFlow.value

    when (val value = uiState.value) {
        is UiState.DataLoaded -> CreateIdentityScreen(
            createIdentityUiModel = value.data,
            showKeyboard = bottomSheetVisibleState == CreateIdentityViewModel.ShowCreateIdentity,
            handleChange = {
                createIdentityViewModel.updateHandle(it)
            },
            nextClick = {
                createIdentityViewModel.nextStep()
            }
        )

        is CreateIdentityViewModel.GoToIdentity -> navigator.navigate(
            MainScreenDestination(
                createdAccount = true
            )
        )
    }


}

@Composable
fun CreateIdentityScreen(
    createIdentityUiModel: CreateIdentityUiModel,
    showKeyboard: Boolean,
    handleChange: (String) -> Unit,
    nextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MainColors.bottomSheetBackground)
            .padding(horizontal = 36.dp, vertical = 12.dp)
    ) {

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            PullDown()
        }

        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = stringResource(
                R.string.create_id_steps,
                createIdentityUiModel.currentStep,
                createIdentityUiModel.totalSteps
            ),
            style = MainTypography.stepCounter,
            color = MainColors.onBottomSheetBackground
        )
        Text(
            text = stringResource(R.string.create_digital_identity),
            style = MainTypography.bodyMedium,
            color = MainColors.onBottomSheetBackground
        )

        when (createIdentityUiModel.currentStep) {
            1 -> CreateHandleScreen(
                handle = createIdentityUiModel.handle,
                handleIsValid = createIdentityUiModel.handleIsValid,
                showKeyboard = showKeyboard,
                handleChange = handleChange,
                nextClick = nextClick
            )

            2 -> ConfirmHandleScreen(
                handle = createIdentityUiModel.handle,
                suffix = createIdentityUiModel.suffix,
                nextClick = nextClick
            )

            3 -> AgreeToTermsScreen(
                handle = createIdentityUiModel.handle,
                suffix = createIdentityUiModel.suffix,
                agreeClick = nextClick
            )
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CreateHandleScreen(
    handle: String,
    handleIsValid: Boolean,
    showKeyboard: Boolean,
    handleChange: (String) -> Unit,
    nextClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    Column {
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = stringResource(R.string.your_unique_handle),
            style = MainTypography.body,
            color = MainColors.onBottomSheetBackground
        )

        Spacer(modifier = Modifier.size(20.dp))
        InputTextField(
            label = stringResource(R.string.claim_your_handle),
            text = handle,
            onTextChange = handleChange,
            focusRequester = focusRequester
        )

        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = stringResource(R.string.handle_rules),
            style = MainTypography.body,
            color = MainColors.onBottomSheetBackground
        )

        Spacer(modifier = Modifier.size(16.dp))
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.next),
            enabled = handleIsValid,
            onClick = nextClick
        )
    }

    LaunchedEffect(showKeyboard) {
        if (showKeyboard) {
            focusRequester.requestFocus()
            delay(100)
            keyboard?.show()
        }
    }
}

@Composable
private fun ConfirmHandleScreen(
    handle: String,
    suffix: String,
    nextClick: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.size(26.dp))
        Text(
            text = stringResource(R.string.confirm_your_handle),
            style = MainTypography.title.copy(lineHeight = 34.sp),
            color = MainColors.onEditTextTitle
        )

        Spacer(modifier = Modifier.size(18.dp))
        Handle(
            handle = handle,
            suffix = suffix,
            handleColor = MainColors.primary
        )

        Spacer(modifier = Modifier.size(30.dp))
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.next),
            onClick = nextClick
        )

        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = stringResource(R.string.numerical_suffix_auto_assigned),
            style = MainTypography.body,
            color = MainColors.onBottomSheetBackground
        )
    }
}

@Composable
private fun AgreeToTermsScreen(
    handle: String,
    suffix: String,
    agreeClick: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.size(26.dp))
        Text(
            text = stringResource(R.string.agree_to_terms),
            style = MainTypography.title.copy(lineHeight = 34.sp),
            color = MainColors.onEditTextTitle
        )

        Spacer(modifier = Modifier.size(18.dp))
        Handle(
            handle = handle,
            suffix = suffix,
            handleColor = MainColors.primary
        )

        Spacer(modifier = Modifier.size(30.dp))
        Text(
            text = stringResource(R.string.by_agreeing),
            style = MainTypography.bodySemiBold,
            color = MainColors.onEditTextTitle
        )
        Bullet(text = stringResource(R.string.update_your_handle_and_profile_information))
        Bullet(text = stringResource(R.string.update_your_contacts_groups))

        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = stringResource(R.string.you_may_update_permissions_at_any_time),
            style = MainTypography.body,
            color = MainColors.onBottomSheetBackground
        )

        Spacer(modifier = Modifier.size(30.dp))
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.agree),
            onClick = agreeClick
        )

        Spacer(modifier = Modifier.size(100.dp))
        TermsAndPrivacy(
            textColor = MainColors.onBottomSheetBackground
        )
    }
}

private val createIdentityUiModel: CreateIdentityUiModel by lazy {
    CreateIdentityUiModel(
        handle = "neverendingwinter"
    )
}

@Preview
@Composable
fun SampleCreateIdentityScreenStep1() {
    MainTheme {
        CreateIdentityScreen(
            createIdentityUiModel = createIdentityUiModel,
            showKeyboard = true,
            handleChange = {},
            nextClick = {}
        )
    }
}

@Preview
@Composable
fun SampleCreateIdentityScreenStep2() {
    MainTheme {
        CreateIdentityScreen(
            createIdentityUiModel = createIdentityUiModel.copy(
                currentStep = 2,
                handleIsValid = true
            ),
            showKeyboard = true,
            handleChange = {},
            nextClick = {}
        )
    }
}

@Preview
@Composable
fun SampleCreateIdentityScreenStep3() {
    MainTheme {
        CreateIdentityScreen(
            createIdentityUiModel = createIdentityUiModel.copy(
                currentStep = 3,
                handleIsValid = true
            ),
            showKeyboard = true,
            handleChange = {},
            nextClick = {}
        )
    }
}