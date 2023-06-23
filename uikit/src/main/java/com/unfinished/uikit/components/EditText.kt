package com.unfinished.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    label: String,
    text: String,
    labelColor: Color = MainColors.grey90,
    onTextChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            style = MainTypography.bodySmall,
            color = labelColor
        )
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp, color = MainColors.grey20, shape = ButtonDefaults.shape
                ),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MainColors.onEditTextBackground,
                containerColor = MainColors.editTextBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = ButtonDefaults.shape
        )
    }
}

@Preview
@Composable
private fun SampleEditTexts() {
    MainTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainColors.bottomSheetBackground)
                .padding(16.dp)
        ) {
            InputTextField(
                label = "Claim your handle",
                text = "neverendingwinter",
                onTextChange = {}
            )
        }
    }
}