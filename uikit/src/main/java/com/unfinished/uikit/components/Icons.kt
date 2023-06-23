package com.unfinished.uikit.components

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.R

@Composable
fun Logo(
    modifier: Modifier = Modifier
) {
    Image(
        modifier = Modifier
            .size(223.dp, 86.dp)
            .then(modifier),
        painter = painterResource(id = R.drawable.amplica_logo),
        contentDescription = stringResource(id = R.string.amplica_logo)
    )
}

@Composable
fun Back(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Icon(
        painter = painterResource(id = R.drawable.back_arrow),
        contentDescription = stringResource(R.string.back),
        modifier = modifier
            .padding(16.dp)
            .clickable(onClick = {
                if (onClick == null) onBackPressedDispatcher?.onBackPressed() else onClick()
            }),
        tint = MainColors.onToolbar
    )
}

@Composable
fun Close(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Icon(
        painter = painterResource(id = R.drawable.close),
        contentDescription = stringResource(R.string.close),
        modifier = modifier
            .padding(16.dp)
            .clickable(onClick = {
                if (onClick == null) onBackPressedDispatcher?.onBackPressed() else onClick()
            }),
        tint = MainColors.onToolbar
    )
}

@Composable
fun Loading(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = Modifier
            .size(100.dp)
            .then(modifier),
        color = MainColors.loading,
        strokeWidth = 4.dp
    )
}

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    iconUrl: String?
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(iconUrl)
            .crossfade(true)
            .transformations(CircleCropTransformation())
            .build(),
        contentDescription = stringResource(id = R.string.profile_icon),
        loading = {
            Loading()
        },
        error = {
            Image(
                modifier = Modifier.clip(CircleShape),
                painter = painterResource(id = R.drawable.ghost_profile),
                contentDescription = stringResource(id = R.string.profile_icon_empty)
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    )
}

@Composable
fun Edit(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = R.drawable.edit),
        contentDescription = stringResource(id = R.string.edit),
        tint = MainColors.onButton
    )
}

@Composable
fun Checkmark(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = R.drawable.checkmark),
        contentDescription = stringResource(id = R.string.checkmark),
        tint = MainColors.primary
    )
}

@Composable
fun PullDown() {
    Box(
        modifier = Modifier
            .size(width = 84.dp, height = 5.dp)
            .clip(ButtonDefaults.shape)
            .background(MainColors.pullDown)
    )
}

@Preview
@Composable
private fun SampleIcons() {
    MainTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainColors.background)
        ) {
            Logo()
            Back()
            Close()
            Loading()
            Edit()
            PullDown()
            Checkmark()
            Profile(iconUrl = null)
        }
    }
}