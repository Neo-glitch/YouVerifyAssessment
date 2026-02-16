package org.neo.yvstore.core.ui.animations

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object EnterExitTransitions {
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

}