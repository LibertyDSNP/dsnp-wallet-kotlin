package com.unfinished.dsnp_wallet_kotlin.ui.home.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.components.PrimaryButton

@Composable
fun CongratulationsScreen(
    username: String,
    letsGoClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MainColors.dialog),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.congratulations),
            color = MainColors.onDialog,
            style = MainTypography.dialogTitle
        )

        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = username,
            color = MainColors.onDialog,
            style = MainTypography.bodySmall
        )

        Spacer(modifier = Modifier.size(26.dp))
        Text(
            text = "lorem ipsum",
            color = MainColors.onDialog,
            style = MainTypography.bodySmall
        )

        Spacer(modifier = Modifier.size(28.dp))
        PrimaryButton(
            modifier = Modifier.width(223.dp),
            text = stringResource(R.string.let_s_go),
            onClick = letsGoClick
        )

        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = stringResource(R.string.skip_for_now),
            color = MainColors.onDialog,
            style = MainTypography.bodySmall,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable(onClick = onDismiss)
        )
    }

}

@Preview
@Composable
fun SampleCongratulationsScreen() {
    MainTheme {
        CongratulationsScreen(
            username = "neverendingwinter.23",
            letsGoClick = {},
            onDismiss = {}
        )
    }
}