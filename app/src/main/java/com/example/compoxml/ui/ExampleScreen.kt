@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.compoxml.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
        MerchantStuff()
    }
}

@Composable
private fun MerchantStuff() {
    Box(
        modifier = Modifier.padding(horizontal = 24.dp).padding(top = 90.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .border(2.dp, color = Color.LightGray, shape = RoundedCornerShape(16.dp))
                .padding(16.dp, 40.dp, 16.dp, 16.dp),
        ) {
            MerchantData()
        }
        Image(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, color = Color.LightGray, shape = CircleShape),
            painter = painterResource(id = R.drawable.header_yeah),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun MerchantData() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Nome da loja")
            Text("Categoria • 99,9 km • Mínimo R$99")
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null)
    }
    Divider(modifier = Modifier.padding(vertical = 12.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
        )
        Text(text = "5,0 (999 avaliações) •")
        Text(text = "Super")
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null)
    }
    Divider(modifier = Modifier.padding(vertical = 12.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Padrão • Hoje, 999-999 min • R$ 99,99")
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null)
    }
}

@Preview
@Composable
private fun MerchantStuffPreview() {
    MerchantStuff()
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
            painter = painterResource(id = R.drawable.header_yeah),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )
        FilledIconButton(
            modifier = Modifier
                .safeDrawingPadding()
                .padding(24.dp),
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
    val indexInCategoriesList =
        carouselCategories.value.indexOfFirst { it.sectionId == sectionIdClicked }
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
