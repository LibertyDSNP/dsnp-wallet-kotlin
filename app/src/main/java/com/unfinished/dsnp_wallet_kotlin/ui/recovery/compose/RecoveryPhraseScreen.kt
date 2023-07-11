package com.unfinished.dsnp_wallet_kotlin.ui.recovery.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.ui.RecoveryNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.RecoveryTestScreenScreenDestination
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel.RecoveryPhraseUiModel
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel.SeedKey
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel.SeedKeyState
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.viewmodel.RecoveryPhraseViewModel
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.UiState
import com.unfinished.uikit.components.HyperlinkText
import com.unfinished.uikit.components.PrimaryButton
import com.unfinished.uikit.components.SimpleToolbar
import com.unfinished.uikit.exts.tag

@RecoveryNavGraph(start = true)
@Destination
@Composable
fun RecoveryPhraseScreen(
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Boolean>,
    recoveryPhraseViewModel: RecoveryPhraseViewModel
) {
    val uiStateFlow = recoveryPhraseViewModel.uiStateFLow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background)
    ) {
        SimpleToolbar(
            title = stringResource(id = R.string.recovery_phrase),
            testTag = Tag.RecoveryPhraseScreen.title
        )
        Spacer(modifier = Modifier.size(16.dp))

        when (val uiState = uiStateFlow.value) {
            is UiState.DataLoaded -> {
                RecoveryPhraseScreen(
                    recoveryPhraseUiModel = uiState.data,
                    nextClick = {
                        recoveryPhraseViewModel.shuffleSeedKeys()
                        navigator.navigate(RecoveryTestScreenScreenDestination)
                    }
                )

                if (uiState.data.seedKeyState == SeedKeyState.Finish) {
                    resultNavigator.setResult(true)
                    resultNavigator.navigateBack()
                }
            }
        }
    }
}

@Composable
fun RecoveryPhraseScreen(
    recoveryPhraseUiModel: RecoveryPhraseUiModel,
    nextClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.security_is_important),
                style = MainTypography.largeButtonText,
                color = MainColors.onBackground,
                modifier = Modifier.tag(Tag.RecoveryPhraseScreen.securityTitle)
            )

            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = stringResource(R.string.back_up_your_recovery_phrase),
                style = MainTypography.bodySemiBold,
                color = MainColors.onBackground,
                modifier = Modifier.tag(Tag.RecoveryPhraseScreen.securityDesc)
            )

            Spacer(modifier = Modifier.size(55.dp))
            Text(
                text = stringResource(R.string.there_will_be_a_test),
                style = MainTypography.largeButtonText,
                color = MainColors.onBackground,
                modifier = Modifier.tag(Tag.RecoveryPhraseScreen.testTitle)
            )

            Spacer(modifier = Modifier.size(12.dp))
            HyperlinkText(
                fullText = stringResource(R.string.please_carefully_write),
                clickableTexts = listOf(stringResource(id = R.string.please_carefully_write_link_text)),
                style = MainTypography.body.copy(color = MainColors.onBackground),
                onClicked = {},
                modifier = Modifier.tag(Tag.RecoveryPhraseScreen.testDesc)
            )

            Spacer(modifier = Modifier.size(22.dp))
            SeedRow(seedKeys = recoveryPhraseUiModel.seedKeys)

            Spacer(modifier = Modifier.size(45.dp))
            PrimaryButton(
                text = stringResource(R.string.i_ve_written_it_down),
                onClick = nextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .tag(Tag.RecoveryPhraseScreen.writtenDown)
            )
        }
    }
}

@Composable
private fun SeedRow(
    seedKeys: List<SeedKey>
) {
    val seedSize = seedKeys.size
    val firstHalf = seedKeys.take(seedSize / 2)
    val secondHalf = seedKeys.takeLast(seedSize / 2)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SeedColumn(seedKeys = firstHalf)
        //Spacer(modifier = Modifier.size(16.dp))
        SeedColumn(seedKeys = secondHalf)
    }
}

@Composable
private fun SeedColumn(seedKeys: List<SeedKey>) {
    Column {
        seedKeys.forEach {
            Text(
                text = "${it.prefix} ${it.key}",
                style = MainTypography.seedText,
                color = MainColors.onBackground,
                modifier = Modifier.tag(
                    "${Tag.RecoveryPhraseScreen.seed}_${it.prefix}"
                )
            )
        }
    }
}

@Preview
@Composable
fun SampleRecoveryPhraseScreen() {
    MainTheme {
        RecoveryPhraseScreen(
            recoveryPhraseUiModel = RecoveryPhraseUiModel(
                seedKeys = listOf(
                    SeedKey(prefix = "01", key = "Satisy"),
                    SeedKey(prefix = "02", key = "Spike"),
                    SeedKey(prefix = "03", key = "Lake"),
                    SeedKey(prefix = "04", key = "Cupcake"),
                    SeedKey(prefix = "05", key = "Bag"),
                    SeedKey(prefix = "06", key = "Turmoil"),
                    SeedKey(prefix = "07", key = "Sunny"),
                    SeedKey(prefix = "08", key = "Rainbow"),
                    SeedKey(prefix = "09", key = "Truck"),
                    SeedKey(prefix = "10", key = "Train"),
                    SeedKey(prefix = "11", key = "Running"),
                    SeedKey(prefix = "12", key = "Spin"),
                ),
                seedKeyState = SeedKeyState.Finish
            ),
            nextClick = {}
        )
    }
}