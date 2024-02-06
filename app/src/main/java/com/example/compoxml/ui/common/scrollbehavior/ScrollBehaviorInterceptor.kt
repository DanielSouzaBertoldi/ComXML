package com.example.compoxml.ui.common.scrollbehavior

import androidx.compose.runtime.Stable
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection

@Stable
interface ScrollBehaviorInterceptor {
    val state: ScrollBehaviorInterceptorState
    val nestedScrollConnection: NestedScrollConnection
}
