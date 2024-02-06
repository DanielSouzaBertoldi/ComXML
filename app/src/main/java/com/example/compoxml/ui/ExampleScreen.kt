@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.compoxml.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compoxml.ui.common.scrollbehavior.ScrollBehaviorInterceptor
import com.example.compoxml.ui.common.scrollbehavior.scrollBehaviorInterceptor
import com.example.compoxml.ui.common.topbar.CustomTopBarLayout

@Stable
class ExampleScreenState(
    val scrollBehaviorInterceptor: ScrollBehaviorInterceptor,
    val exitUntilCollapsedScrollBehavior: TopAppBarScrollBehavior,
    val hasCollapsedHeader: State<Boolean>,
)

@Composable
fun rememberExampleScreenState(
    scrollBehaviorInterceptor: ScrollBehaviorInterceptor = scrollBehaviorInterceptor(),
    exitUntilCollapsedScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
) = remember {
    ExampleScreenState(
        scrollBehaviorInterceptor = scrollBehaviorInterceptor,
        exitUntilCollapsedScrollBehavior = exitUntilCollapsedScrollBehavior,
        hasCollapsedHeader = derivedStateOf {
            scrollBehaviorInterceptor.state.scrollOffset.floatValue < 0
        }
    )
}

@Composable
fun ExampleScreen(state: ExampleScreenState = rememberExampleScreenState()) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(state.exitUntilCollapsedScrollBehavior.nestedScrollConnection)
            .nestedScroll(state.scrollBehaviorInterceptor.nestedScrollConnection),
        topBar = {
            Header(state)
            SearchBar(state)
        },
        content = { Content(paddingValues = it) }
    )
}

@Composable
private fun Content(paddingValues: PaddingValues) {
    //Recycler
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        items(100) { index ->
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Item $index",
            )
        }
    }
}

@Composable
private fun SearchBar(state: ExampleScreenState) {
    //https://developer.android.com/jetpack/compose/animation
    AnimatedVisibility(
        visible = state.hasCollapsedHeader.value,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .height(144.dp)
                .fillMaxWidth()
                .background(color = Color.Red)
        )
    }
}

@Composable
private fun Header(state: ExampleScreenState) {
    CustomTopBarLayout(scrollBehavior = state.exitUntilCollapsedScrollBehavior) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(144.dp),
            color = Color.Blue,
            content = {}
        )
    }
}

@Preview
@Composable
private fun PreviewExample() {
    ExampleScreen()
}
