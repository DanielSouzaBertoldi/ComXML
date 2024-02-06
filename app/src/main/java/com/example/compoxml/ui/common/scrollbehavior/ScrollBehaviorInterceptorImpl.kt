package com.example.compoxml.ui.common.scrollbehavior

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource


class ScrollBehaviorInterceptorImpl(
    override val state: ScrollBehaviorInterceptorState,
) : ScrollBehaviorInterceptor {
    override val nestedScrollConnection =
        object : NestedScrollConnection {

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                state.scrollOffset.floatValue += consumed.y
                if (consumed.y == 0f && available.y > 0) {
                    // Reset the total content offset to zero when scrolling all the way down. This
                    // will eliminate some float precision inaccuracies.
                    state.scrollOffset.floatValue = 0f
                }
                return Offset.Zero
            }
        }
}

@Composable
fun scrollBehaviorInterceptor(
    state: ScrollBehaviorInterceptorState = rememberScrollBehaviorInterceptorState(),
): ScrollBehaviorInterceptor = ScrollBehaviorInterceptorImpl(state = state)