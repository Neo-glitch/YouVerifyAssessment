package org.neo.yvstore.core.ui.component.grid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NonlazyGrid(
    columns: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp,
    content: @Composable (Int) -> Unit,
) {
    val rowCount = calculateRowCount(itemCount, columns)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
    ) {
        repeat(rowCount) { rowIndex ->
            GridRow(
                rowIndex = rowIndex,
                columns = columns,
                itemCount = itemCount,
                horizontalSpacing = horizontalSpacing,
                content = content,
            )
        }
    }
}

@Composable
private fun GridRow(
    rowIndex: Int,
    columns: Int,
    itemCount: Int,
    horizontalSpacing: Dp,
    content: @Composable (Int) -> Unit,
) {
    val startIndex = rowIndex * columns

    Row(
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
    ) {
        repeat(columns) { columnIndex ->
            val itemIndex = startIndex + columnIndex

            GridCell(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                if (itemIndex < itemCount) {
                    content(itemIndex)
                }
            }
        }
    }
}

@Composable
private fun GridCell(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        content()
    }
}

private fun calculateRowCount(itemCount: Int, columns: Int): Int {
    return (itemCount + columns - 1) / columns
}
