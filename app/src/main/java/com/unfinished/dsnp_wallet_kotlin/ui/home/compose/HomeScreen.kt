package com.unfinished.dsnp_wallet_kotlin.ui.home.compose

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.components.Edit
import com.unfinished.uikit.components.Overlay
import com.unfinished.uikit.components.Profile
import com.unfinished.uikit.components.RoundedProgressBar

@Composable
fun HomeScreen(
    iconUrl: String?,
    username: String,
    currentCount: Int,
    totalCount: Int
) {
    Column(
        modifier = Modifier.background(MainColors.background)
    ) {
        ProfileRow(
            iconUrl = iconUrl,
            username = username
        )

        Spacer(modifier = Modifier.size(52.dp))
        Box {
            SocialProcessRow(
                currentCount = currentCount,
                totalCount = totalCount
            )
            Overlay(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun ProfileRow(
    iconUrl: String?,
    username: String
) {
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
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Edit()
            }
        }

        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = username,
            style = MainTypography.profile,
            color = MainColors.onBackground
        )
        Spacer(modifier = Modifier.size(12.dp))
    }
}

@Composable
private fun SocialProcessRow(
    currentCount: Int,
    totalCount: Int
) {
    val countDisplay = "$currentCount/$totalCount"
    val progress = currentCount.toFloat() / totalCount.toFloat()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 55.dp),
    ) {
        Spacer(modifier = Modifier.size(32.dp))
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

        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.see_all),
            style = MainTypography.textLink,
            color = MainColors.hyperLink,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Preview
@Composable
private fun SampleHomeScreen() {
    MainTheme {
        HomeScreen(
            iconUrl = null,
            username = "neverendingwinter.23",
            currentCount = 1,
            totalCount = 3
        )
    }
}