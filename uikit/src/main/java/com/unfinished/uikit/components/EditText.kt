package com.unfinished.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainShapes
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    labelColor: Color = MainColors.grey90,
    focusRequester: FocusRequester? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onTextChange: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
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
                    width = 1.dp, color = MainColors.grey20, shape = MainShapes.button
                )
                .then(
                    if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier
                ),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MainColors.onEditTextBackground,
                containerColor = MainColors.editTextBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = MainShapes.button,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedText(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    isError: Boolean = false,
    onTextChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = MainColors.import,
            textColor = MainColors.onImport,
            cursorColor = MainColors.onImport,
            errorBorderColor = MainColors.error,
            errorLabelColor = MainColors.import
        ),
        placeholder = {
            Text(
                text = hint,
                style = MainTypography.stepCounter,
                color = MainColors.onHintImport
            )
        },
        shape = MainShapes.input,
        textStyle = MainTypography.stepCounter,
        isError = isError
    )
}

@Preview
@Composable
private fun SampleEditTexts() {
    MainTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainColors.background)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MainColors.bottomSheetBackground)
                    .padding(16.dp)
            ) {
                InputTextField(
                    label = "Claim your handle",
                    text = "neverendingwinter",
                    onTextChange = {}
                )
            }

            Spacer(modifier = Modifier.size(16.dp))
            OutlinedText(
                text = "",
                hint = "Placeholder",
                onTextChange = {}
            )

            Spacer(modifier = Modifier.size(16.dp))
            OutlinedText(
                text = "Actual text",
                hint = "Placeholder",
                onTextChange = {}
            )

            Spacer(modifier = Modifier.size(16.dp))
            OutlinedText(
                text = "Actual text",
                hint = "Placeholder",
                isError = true,
                onTextChange = {}
            )
        }
    }
}