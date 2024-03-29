package com.unfinished.dsnp_wallet_kotlin.ui.recovery.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.ui.RecoveryNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.common.snackbar.viewmodel.SnackbarViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.home.viewmmodel.IdentityViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel.RecoveryPhraseUiModel
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel.SeedKey
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel.SeedKeyState
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.viewmodel.RecoveryPhraseViewModel
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainShapes
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.UiState
import com.unfinished.uikit.components.Grid
import com.unfinished.uikit.components.Loading
import com.unfinished.uikit.components.PrimaryButton
import com.unfinished.uikit.components.SimpleToolbar
import com.unfinished.uikit.exts.dashedBorder
import com.unfinished.uikit.exts.tag

@RecoveryNavGraph
@Destination
@Composable
fun RecoveryTestScreenScreen(
    navigator: DestinationsNavigator,
    snackbarViewModel: SnackbarViewModel,
    identityViewModel: IdentityViewModel,
    recoveryPhraseViewModel: RecoveryPhraseViewModel
) {
    val uiStateFlow = recoveryPhraseViewModel.uiStateFLow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background)
    ) {
        SimpleToolbar(
            title = stringResource(R.string.verify_recovery),
            testTag = Tag.RecoveryTestScreenScreen.title
        )

        when (val uiState = uiStateFlow.value) {
            is UiState.DataLoaded -> {
                RecoveryTestScreenScreen(
                    recoveryPhraseUiModel = uiState.data,
                    addSeedKeyClick = {
                        recoveryPhraseViewModel.addSeedKeyToGuess(it)
                    },
                    removeSeedKeyClick = {
                        recoveryPhraseViewModel.removeSeedKeyFromGuess(it)
                    },
                    continueClick = { recoveryPhraseViewModel.verifySeedKeys() }
                )

                if (uiState.data.seedKeyState == SeedKeyState.Finish) {
                    snackbarViewModel.showSuccess(stringResource(R.string.congratulations))
                    identityViewModel.backUpSeedCompleted()
                    navigator.popBackStack()
                }
            }
        }
    }
}

@Composable
fun RecoveryTestScreenScreen(
    recoveryPhraseUiModel: RecoveryPhraseUiModel,
    addSeedKeyClick: (SeedKey) -> Unit,
    removeSeedKeyClick: (SeedKey) -> Unit,
    continueClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background)
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.this_is_the_test),
                style = MainTypography.largeButtonText,
                color = MainColors.onBackground,
                modifier = Modifier.tag(Tag.RecoveryTestScreenScreen.header)
            )

            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = stringResource(R.string.tap_the_words),
                style = MainTypography.body,
                color = MainColors.onBackground,
                modifier = Modifier.tag(Tag.RecoveryTestScreenScreen.desc)
            )

            Spacer(modifier = Modifier.size(36.dp))
            SeedTestRow(
                seedKeys = recoveryPhraseUiModel.seedKeys,
                currentSeedGuesses = recoveryPhraseUiModel.currentSeedGuesses,
                currentRandomSeedKeys = recoveryPhraseUiModel.currentRandomSeedKeys,
                addSeedKeyClick = addSeedKeyClick,
                removeSeedKeyClick = removeSeedKeyClick,
                seedKeyState = recoveryPhraseUiModel.seedKeyState
            )

            Spacer(modifier = Modifier.size(36.dp))

            when (recoveryPhraseUiModel.seedKeyState) {
                SeedKeyState.Init, SeedKeyState.NotValid -> PrimaryButton(
                    text = stringResource(R.string.continue_text),
                    onClick = continueClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .tag(Tag.RecoveryTestScreenScreen.continueBtn),
                    enabled = recoveryPhraseUiModel.continueEnabled
                )

                SeedKeyState.Verifying, SeedKeyState.Finish -> Box(modifier = Modifier.fillMaxWidth()) {
                    Loading(modifier = Modifier.align(Alignment.Center))
                }
            }


        }
    }
}

@Composable
private fun SeedTestRow(
    seedKeys: List<SeedKey>,
    currentSeedGuesses: List<SeedKey?>,
    currentRandomSeedKeys: List<SeedKey>,
    seedKeyState: SeedKeyState,
    addSeedKeyClick: (SeedKey) -> Unit,
    removeSeedKeyClick: (SeedKey) -> Unit
) {
    val seedSize = seedKeys.size
    val firstHalf = seedKeys.take(seedSize / 2)
    val secondHalf = seedKeys.takeLast(seedSize / 2)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SeedTestColumn(
                modifier = Modifier.weight(.45F),
                seedKeys = firstHalf,
                currentSeedGuesses = currentSeedGuesses,
                offsetIndex = 0,
                removeSeedKeyClick = removeSeedKeyClick
            )
            Spacer(modifier = Modifier.weight(.05F))
            SeedTestColumn(
                modifier = Modifier.weight(.45F),
                seedKeys = secondHalf,
                currentSeedGuesses = currentSeedGuesses,
                offsetIndex = firstHalf.size,
                removeSeedKeyClick = removeSeedKeyClick
            )
        }

        Text(
            text = if (seedKeyState == SeedKeyState.NotValid) stringResource(R.string.you_got_it_wrong) else "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .tag(Tag.RecoveryTestScreenScreen.error),
            textAlign = TextAlign.Center,
            color = MainColors.error,
            style = MainTypography.bodyMediumBold
        )

        Grid(
            modifier = Modifier,
            columns = 3,
            itemCount = seedSize,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = { index ->
                val currentSeed = currentRandomSeedKeys[index]
                val seedToUse =
                    if (currentSeedGuesses.contains(currentSeed)) null else currentSeed
                SeedGuessButton(
                    seedKey = seedToUse,
                    addSeedKeyClick = addSeedKeyClick,
                    prefix = currentSeed.prefix
                )
            }
        )
    }
}

@Composable
private fun SeedTestColumn(
    modifier: Modifier = Modifier,
    seedKeys: List<SeedKey>,
    currentSeedGuesses: List<SeedKey?>,
    offsetIndex: Int = 0,
    removeSeedKeyClick: (SeedKey) -> Unit
) {
    Column(
        modifier = modifier
    ) {

        seedKeys.forEachIndexed { index, seedKey ->
            val currentIndex = index + offsetIndex

            SeedGuessItem(
                seedKey = currentSeedGuesses[currentIndex],
                prefix = seedKey.prefix,
                removeSeedKeyClick = removeSeedKeyClick
            )

            if (index != seedKeys.lastIndex) Spacer(modifier = Modifier.size(4.dp))
        }
    }
}

@Composable
private fun SeedGuessItem(
    seedKey: SeedKey?,
    prefix: String,
    removeSeedKeyClick: (SeedKey) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MainShapes.button)
            .border(
                width = 1.dp,
                color = MainColors.seedChoice,
                shape = MainShapes.button
            )
            .background(if (seedKey != null) MainColors.seedChoice else Color.Transparent)
            .padding(vertical = 4.dp)
            .tag("${Tag.RecoveryTestScreenScreen.guessSeed}_$prefix")
            .then(
                if (seedKey != null) Modifier.clickable {
                    removeSeedKeyClick(seedKey)
                } else Modifier
            )
    ) {
        Text(
            text = prefix,
            style = MainTypography.seedTextBold,
            color = MainColors.onSeedChoice,
            modifier = Modifier.padding(start = 12.dp)
        )

        if (seedKey != null) Text(
            modifier = Modifier
                .weight(1F)
                .offset(x = (-12).dp),
            text = seedKey.key,
            textAlign = TextAlign.Center,
            style = MainTypography.seedText,
            color = MainColors.onSeedChoice,
            maxLines = 1
        )
    }
}

@Composable
private fun SeedGuessButton(
    seedKey: SeedKey?,
    prefix: String,
    addSeedKeyClick: (SeedKey) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MainShapes.button)
            .then(
                if (seedKey != null) Modifier.background(MainColors.seedChoice)
                else Modifier
                    .dashedBorder(
                        width = 1.dp,
                        shape = MainShapes.button,
                        color = MainColors.seedChoice,
                        on = 2.dp,
                        off = 2.dp
                    )
                    .background(Color.Transparent)
            )
            .padding(vertical = 4.dp)
            .tag("${Tag.RecoveryTestScreenScreen.choiceSeed}_$prefix")
            .then(
                if (seedKey != null) Modifier.clickable {
                    addSeedKeyClick(seedKey)
                } else Modifier
            )
    ) {
        Text(
            modifier = Modifier.weight(1F),
            text = seedKey?.key ?: "",
            textAlign = TextAlign.Center,
            style = MainTypography.seedText,
            color = MainColors.onSeedChoice,
            maxLines = 1
        )
    }
}

private val sampleRecoveryPhraseUiModel: RecoveryPhraseUiModel by lazy {
    val seedKeys = listOf(
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
    )

    val uiModel = RecoveryPhraseUiModel(
        seedKeys = seedKeys,
        currentRandomSeedKeys = seedKeys.shuffled(),
        seedKeyState = SeedKeyState.NotValid
    )

    return@lazy uiModel.copy(
        currentSeedGuesses = uiModel.currentSeedGuesses.toMutableList().apply {
            removeAt(4)
            add(4, SeedKey(prefix = "06", key = "Turmoil"))

            removeAt(1)
            add(1, SeedKey(prefix = "11", key = "Running"))

            removeAt(2)
            add(2, SeedKey(prefix = "02", key = "Spike"))
        }
    )
}

@Preview
@Composable
fun SampleRecoveryTestScreenScreen() {
    MainTheme {
        RecoveryTestScreenScreen(
            recoveryPhraseUiModel = sampleRecoveryPhraseUiModel,
            removeSeedKeyClick = {},
            addSeedKeyClick = {},
            continueClick = {}
        )
    }
}