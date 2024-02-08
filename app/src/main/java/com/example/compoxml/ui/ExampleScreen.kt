@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.compoxml.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.compoxml.R
import com.example.compoxml.ui.common.topbar.CustomTopBarLayout
import com.example.compoxml.ui.common.topbar.CustomTopBarLayoutDefaults

@Stable
class ExampleScreenState(
    val exitUntilCollapsedScrollBehavior: TopAppBarScrollBehavior,
    val headerCollapsedFraction: State<Float>,
)

@Composable
fun rememberExampleScreenState(
    exitUntilCollapsedScrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(snapAnimationSpec = null),
) = remember {
    ExampleScreenState(
        exitUntilCollapsedScrollBehavior = exitUntilCollapsedScrollBehavior,
        headerCollapsedFraction = derivedStateOf {
            exitUntilCollapsedScrollBehavior.state.collapsedFraction
        }
    )
}

@Composable
fun ExampleScreen(state: ExampleScreenState = rememberExampleScreenState()) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(state.exitUntilCollapsedScrollBehavior.nestedScrollConnection),
        topBar = {
            Header(state)
            SearchBar(state)
        },
        content = {
            Box {
                BannerTopBar(state)
                Content(paddingValues = it)
            }
        }
    )
}

@Composable
private fun BannerTopBar(state: ExampleScreenState) {
    Box(
        modifier = Modifier.graphicsLayer {
            alpha = 1 - (state.headerCollapsedFraction.value * 2)
        }
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(144.dp),
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
        FilledIconButton(
            modifier = Modifier.padding(24.dp),
            onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
    }
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
private fun SearchBar(
    state: ExampleScreenState,
    height: Dp = 96.dp
) {
    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .offset(y = -height)
            .offset {
                // You can manipulate the offset speed and threshold based on the
                // headerCollapsedFraction value
                val offset = (state.headerCollapsedFraction.value) * height.toPx()
                IntOffset(x = 0, y = offset.toInt())
            }
            .background(Color.Blue),
        content = {}
    )
}

@Composable
private fun Header(state: ExampleScreenState) {
    CustomTopBarLayout(
        scrollBehavior = state.exitUntilCollapsedScrollBehavior,
        colors = CustomTopBarLayoutDefaults.colors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
        ),
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 120.dp, bottom = 24.dp)
                .fillMaxWidth()
                .height(144.dp),
            shadowElevation = 2.dp,
            tonalElevation = 1.dp,
            shape = MaterialTheme.shapes.medium,
            content = {}
        )
    }
}

@Preview
@Composable
private fun PreviewExample() {
    ExampleScreen()
}
