package com.unfinished.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography

@Composable
fun Handle(
    handle: String,
    suffix: String,
    handleStyle: TextStyle = MainTypography.profile,
    handleColor: Color = MainColors.onBackground,
    suffixStyle: TextStyle = MainTypography.profileSecondary,
    suffixColor: Color = MainColors.onBackgroundSecondary
) {
    Row {
        Text(
            text = handle,
            style = handleStyle,
            color = handleColor
        )
        Text(
            text = ".$suffix",
            style = suffixStyle,
            color = suffixColor
        )
    }
}

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

@Composable
fun Bullet(
    text: String,
    textStyle: TextStyle = MainTypography.body,
    color: Color = MainColors.grey90
) {
    Row {
        Spacer(modifier = Modifier.size(4.dp))
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(color)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = text,
            modifier = Modifier,
            style = textStyle,
            color = color
        )
    }
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
            Handle(
                handle = "neverendingwinter",
                suffix = "23"
            )

            Spacer(modifier = Modifier.size(16.dp))
            HyperlinkText(
                modifier = Modifier,
                fullText = "Showcasing how hyperlink works with compose",
                clickableTexts = listOf("hyperlink", "compose"),
                onClicked = {}
            )

            Spacer(modifier = Modifier.size(16.dp))
            Bullet(
                text = "Sample Text"
            )
        }
    }
}
