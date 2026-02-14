package org.neo.yvstore.core.ui.component.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun YVStoreMultiStyleText(
    fullText: String,
    parts: List<StyledPart>,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    defaultStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val annotated = buildAnnotatedString {
        append(fullText)

        parts.forEach { part ->
            var startIndex = fullText.indexOf(part.value)

            while (startIndex != -1) {
                val endIndex = startIndex + part.value.length

                addStyle(
                    style = part.style,
                    start = startIndex,
                    end = endIndex,
                )

                startIndex = fullText.indexOf(part.value, startIndex + 1)
            }
        }
    }

    Text(
        text = annotated,
        style = defaultStyle,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow,
    )
}

data class StyledPart(
    val value: String,
    val style: SpanStyle,
)
