package com.unfinished.dsnp_wallet_kotlin.ui.setting.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.components.Alert
import com.unfinished.uikit.components.CloseableDialog
import com.unfinished.uikit.components.PrimaryButton
import com.unfinished.uikit.components.SecondaryButton

@Composable
fun LogoutScreen(
    logoutClick: () -> Unit,
    viewRecoveryPhraseClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MainColors.dialog),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Alert()
        Text(
            text = stringResource(id = com.unfinished.uikit.R.string.log_out),
            color = MainColors.onDialog,
            style = MainTypography.dialogTitle,
            modifier = Modifier.testTag(Tag.LogoutScreen.header)
        )

        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.logout_dialog_desc),
            color = MainColors.onDialog,
            style = MainTypography.bodySmall,
            modifier = Modifier.testTag(Tag.LogoutScreen.desc)
        )

        Spacer(modifier = Modifier.size(28.dp))
        PrimaryButton(
            modifier = Modifier
                .width(223.dp)
                .testTag(Tag.LogoutScreen.primaryButton),
            text = stringResource(com.unfinished.uikit.R.string.log_out),
            onClick = logoutClick
        )

        Spacer(modifier = Modifier.size(12.dp))
        SecondaryButton(
            modifier = Modifier
                .width(223.dp)
                .testTag(Tag.LogoutScreen.secondaryButton),
            text = stringResource(R.string.view_recovery_phrase),
            onClick = viewRecoveryPhraseClick,
            textColor = MainColors.primary
        )
    }
}

@Preview
@Composable
fun SampleLogoutScreen() {
    MainTheme {
        LogoutScreen(
            logoutClick = {},
            viewRecoveryPhraseClick = {}
        )
    }
}

@Preview
@Composable
fun SampleLogoutDialog() {
    MainTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            CloseableDialog(
                content = {
                    LogoutScreen(
                        logoutClick = {},
                        viewRecoveryPhraseClick = {}
                    )
                },
                onDismiss = { }
            )
        }
    }
}