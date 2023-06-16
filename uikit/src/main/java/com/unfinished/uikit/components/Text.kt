package com.unfinished.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography

@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier,
    fullText: String,
    clickableTexts: List<String>,
    clickableTextColor: Color = MainColors.hyperLink,
    style: TextStyle = MainTypography.body.copy(color = MainColors.onBackground),
    clickableStyle: TextStyle? = null,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    onClicked: (String) -> Unit
) {
    val annotatedString = createAnnotatedString(
        fullText = fullText,
        clickableTexts = clickableTexts,
        clickableTextColor = clickableTextColor,
        clickableStyle = clickableStyle
    )

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        style = style,
        onClick = {
            clickableTexts.forEach { clickableText ->
                annotatedString.getStringAnnotations(clickableText, it, it + clickableText.length)
                    .firstOrNull()?.let {
                        onClicked(clickableText)
                        return@ClickableText
                    }
            }
        },
        onTextLayout = onTextLayout
    )
}

private fun createAnnotatedString(
    fullText: String,
    clickableTexts: List<String>,
    clickableTextColor: Color,
    clickableStyle: TextStyle? = null
): AnnotatedString =
    buildAnnotatedString {
        append(fullText)

        clickableTexts.forEach {
            val startIndex = fullText.indexOf(it)
            val endIndex = startIndex + it.length

            addStyle(
                style = SpanStyle(
                    color = clickableTextColor,
                    textDecoration = TextDecoration.None,
                    fontStyle = clickableStyle?.fontStyle,
                    fontWeight = clickableStyle?.fontWeight,
                    fontFamily = clickableStyle?.fontFamily
                ), start = startIndex, end = endIndex
            )
            addStringAnnotation(
                tag = it,
                annotation = "",
                start = startIndex,
                end = endIndex
            )
        }

    }

@Preview
@Composable
private fun SampleTexts() {
    MainTheme {
        Column(
            modifier = Modifier.background(MainColors.background)
        ) {
            HyperlinkText(
                modifier = Modifier,
                fullText = "Showcasing how hyperlink works with compose",
                clickableTexts = listOf("hyperlink", "compose"),
                onClicked = {}
            )
        }
    }
}
