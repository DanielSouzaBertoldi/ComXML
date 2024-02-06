package com.example.compoxml.ui.common.scrollbehavior

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember

@Stable
class ScrollBehaviorInterceptorState(
    val scrollOffset: MutableFloatState,
)

@Composable
fun rememberScrollBehaviorInterceptorState() = remember {
    ScrollBehaviorInterceptorState(
        scrollOffset = mutableFloatStateOf(0f)
    )
}
