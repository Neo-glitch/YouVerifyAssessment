package org.neo.yvstore.core.ui.animations

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

object EnterExitTransitions {

    const val FAST = 150
    const val NORMAL = 300
    const val SLOW = 500

    val FadeIn = fadeIn(animationSpec = tween(300))
    val FadeOut = fadeOut(animationSpec = tween(300))

    val SlideInFromRight = slideInHorizontally(
        animationSpec = tween(300),
        initialOffsetX = { it },
    )

    val SlideOutToLeft = slideOutHorizontally(
        animationSpec = tween(300),
        targetOffsetX = { -it },
    )

    val SlideInFromLeft = slideInHorizontally(
        animationSpec = tween(300),
        initialOffsetX = { -it },
    )

    val SlideOutToRight = slideOutHorizontally(
        animationSpec = tween(300),
        targetOffsetX = { it },
    )

    val slideInFromBottom = slideInVertically(
        animationSpec = tween(300),
        initialOffsetY = { it },
    )

    val SlideOutToBottom = slideOutVertically(
        animationSpec = tween(300),
        targetOffsetY = { it },
    )

    // Combined transitions
    val FadeSlideInFromRight = SlideInFromRight + FadeIn
    val FadeSlideOutToLeft = SlideOutToLeft + FadeOut
    val FadeSlideInFromLeft = SlideInFromLeft + FadeIn
    val FadeSlideOutToRight = SlideOutToRight + FadeOut
    val FadeSlideInFromBottom = slideInFromBottom + FadeIn
    val FadeSlideOutToBottom = SlideOutToBottom + FadeOut
}