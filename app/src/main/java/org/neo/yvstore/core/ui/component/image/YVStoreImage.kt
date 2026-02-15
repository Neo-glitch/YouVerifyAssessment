package org.neo.yvstore.core.ui.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme

@Composable
fun YVStoreImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    clipToBounds: Boolean = true,
    contentScale: ContentScale = ContentScale.Crop,
) {
    if (imageUrl.isNullOrEmpty()) {
        ErrorPlaceholder(modifier = modifier)
        return
    }

    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        clipToBounds = clipToBounds,
        contentScale = contentScale,
    ) {
        when (painter.state) {
            is coil.compose.AsyncImagePainter.State.Loading -> {
                LoadingPlaceholder()
            }
            is coil.compose.AsyncImagePainter.State.Error -> {
                ErrorPlaceholder()
            }
            else -> {
                SubcomposeAsyncImageContent()
            }
        }
    }
}

@Composable
private fun LoadingPlaceholder(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(R.drawable.ic_gallery_image),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
    )
}

@Composable
private fun ErrorPlaceholder(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(R.drawable.ic_broken_image),
        contentDescription = "Failed to load image",
        modifier = modifier.fillMaxSize(),
        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
    )
}

@Preview(showBackground = true)
@Composable
private fun YVStoreImagePreview() {
    YVStoreTheme {
        YVStoreImage(
            imageUrl = "https://picsum.photos/400/400",
            contentDescription = "Sample product image",
            modifier = Modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStoreImageErrorPreview() {
    YVStoreTheme {
        YVStoreImage(
            imageUrl = null,
            contentDescription = "Missing product image",
            modifier = Modifier,
        )
    }
}
