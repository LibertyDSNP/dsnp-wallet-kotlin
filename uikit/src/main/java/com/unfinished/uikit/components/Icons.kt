package com.unfinished.uikit.components

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        imageVector = Icons.Filled.ArrowBack,
        contentDescription = stringResource(R.string.back),
        modifier = modifier
            .padding(16.dp)
            .clickable(onClick = {
                if (onClick == null) onBackPressedDispatcher?.onBackPressed() else onClick()
            })
    )
}

@Preview
@Composable
private fun SampleIcons() {
    MainTheme {
        Column {
            Logo()
            Back()
        }
    }
}