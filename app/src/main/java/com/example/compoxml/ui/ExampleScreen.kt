@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.compoxml.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.compoxml.R
import com.example.compoxml.recycler.HeaderUIModel
import com.example.compoxml.recycler.MockAdapter
import com.example.compoxml.recycler.MockListUIModel
import com.example.compoxml.recycler.createMock
import com.example.compoxml.ui.common.topbar.CustomTopBarLayout
import com.example.compoxml.ui.common.topbar.CustomTopBarLayoutDefaults
import kotlinx.coroutines.flow.MutableStateFlow

@Stable
class ExampleScreenState(
    val exitUntilCollapsedScrollBehavior: TopAppBarScrollBehavior,
    val headerCollapsedFraction: State<Float>,
    val currentCategoryIndex: MutableStateFlow<Int>,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberExampleScreenState(
    exitUntilCollapsedScrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(snapAnimationSpec = null),
) = remember {
    ExampleScreenState(
        exitUntilCollapsedScrollBehavior = exitUntilCollapsedScrollBehavior,
        headerCollapsedFraction = derivedStateOf {
            exitUntilCollapsedScrollBehavior.state.collapsedFraction
        },
        currentCategoryIndex = MutableStateFlow(0),
    )
}

private val listMock = createMock()
private val listAdapter = MockAdapter()

@Composable
fun ExampleScreen(state: ExampleScreenState = rememberExampleScreenState()) {
    val context = LocalContext.current

    // Fetches List of Carousel Headers (e.g. "Category 1", "Category 2", ...)
    val carouselCategories = remember { mutableStateOf(fetchCarouselHeadersFromList()) }

    // Recycler Stuff
    val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference() = SNAP_TO_START
    }
    val linearLayout = LinearLayoutManager(context)
    val recyclerViewRef = RecyclerView(context).apply {
        layoutManager = linearLayout
        this.adapter = listAdapter
        listAdapter.submitList(listMock)
    }
    recyclerViewRef.addOnScrollListener(
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItem = linearLayout.findFirstVisibleItemPosition()
                val item = listAdapter.currentList[firstVisibleItem]
                if (item is MockListUIModel.Header) {
                    // Searching in the carousel list which item has the same Section ID
                    // as the first visible item in the RecyclerView
                    state.currentCategoryIndex.value = carouselCategories.value.indexOfFirst {
                        it.sectionId == item.sectionId
                    }
                }
            }
        }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(state.exitUntilCollapsedScrollBehavior.nestedScrollConnection),
        topBar = {
            Header(state)
            Toolbar(
                state = state,
                carouselCategories = carouselCategories,
                carouselCategoryClick = { clickedCategorySectionId ->
                    smoothScrollTo(
                        smoothScroller = smoothScroller,
                        linearLayoutManager = linearLayout,
                        clickedSectionId = clickedCategorySectionId,
                    )
                    setItemAsSelected(
                        sectionIdClicked = clickedCategorySectionId,
                        carouselCategories = carouselCategories,
                        onCarouselChange = {
                            carouselCategories.value = it
                        }
                    )
                },
                setItemAsSelected = {
                    carouselCategories.value = it
                },
            )
        },
        content = {
            Box {
                BannerTopBar(state)
                Content(paddingValues = it, recyclerViewRef)
            }
        },
        containerColor = Color.White
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
            color = Color.Yellow,
            content = {}
        )
    }
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
                .height(164.dp),
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
        FilledIconButton(
            modifier = Modifier.safeDrawingPadding().padding(24.dp),
            onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
    }
}

@Composable
private fun Content(paddingValues: PaddingValues, recycler: RecyclerView) {
    // Recycler
    AndroidView(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth(),
        factory = { recycler }
    )
}

private fun fetchCarouselHeadersFromList() = listMock
    .filterIsInstance<MockListUIModel.Header>()
    .map {
        HeaderUIModel(
            sectionId = it.sectionId,
            name = it.name,
        )
    }

private fun smoothScrollTo(
    smoothScroller: RecyclerView.SmoothScroller,
    linearLayoutManager: LinearLayoutManager,
    clickedSectionId: Int,
) {
    // fetching the index of the clicked carousel category inside the
    // listMock, which contains both Header + Items
    val listIndex = listMock.indexOfFirst {
        (it as? MockListUIModel.Header)?.sectionId == clickedSectionId
    }

    smoothScroller.targetPosition = listIndex
    linearLayoutManager.startSmoothScroll(smoothScroller)
}

fun setItemAsSelected(
    sectionIdClicked: Int,
    carouselCategories: State<List<HeaderUIModel>>,
    onCarouselChange: (List<HeaderUIModel>) -> Unit,
) {
    val indexInCategoriesList = carouselCategories.value.indexOfFirst { it.sectionId == sectionIdClicked }
    val oldList = carouselCategories.value.toMutableList().apply {
        this.forEachIndexed { index, it ->
            this[index] = it.copy(isSelected = index == indexInCategoriesList)
        }
    }
    onCarouselChange(oldList)
}

@Preview
@Composable
private fun PreviewExample() {
    ExampleScreen()
}
