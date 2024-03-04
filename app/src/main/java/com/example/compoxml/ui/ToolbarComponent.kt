package com.example.compoxml.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compoxml.recycler.HeaderUIModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Toolbar(
    state: ExampleScreenState,
    carouselCategories: State<List<HeaderUIModel>>,
    carouselCategoryClick: (Int) -> Unit,
    setItemAsSelected: (List<HeaderUIModel>) -> Unit,
) {
    val statusBarHeight = WindowInsets.statusBars.getTop(LocalDensity.current)
    var height by remember { mutableIntStateOf(10) }
    var toolbarHeight by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = -(toolbarHeight.dp))
            .offset {
                // You can manipulate the offset speed and threshold based on the
                // headerCollapsedFraction value
                val offset = (state.headerCollapsedFraction.value) * toolbarHeight.dp.toPx()
                IntOffset(x = 0, y = offset.toInt())
            }
            .onGloballyPositioned { toolbarHeight = it.size.height }
            .background(Color.White),
    ) {
        SearchBar()
        CategoriesCarrousel(
            lastVisibleRecyclerHeaderId = state.currentCategoryIndex.collectAsState(),
            carouselCategories = carouselCategories,
            carouselClick = carouselCategoryClick,
            setItemAsSelected = setItemAsSelected,
        )
    }
}

@Composable
private fun SearchBar() {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color(0xfff5f5f5),
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = Color.Red,
            )
        }

        val containerColor = Color(245, 245, 245)
        OutlinedTextField(
            modifier = Modifier.padding(start = 8.dp),
            maxLines = 1,
            value = text,
            placeholder = {
                Text(
                    text = "Search Something",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = Color.Red,
                )
            },
            onValueChange = { text = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp),
        )
    }
}

@Composable
private fun CategoriesCarrousel(
    lastVisibleRecyclerHeaderId: State<Int>,
    carouselCategories: State<List<HeaderUIModel>>,
    carouselClick: (Int) -> Unit,
    setItemAsSelected: (List<HeaderUIModel>) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(lastVisibleRecyclerHeaderId.value) {
        delay(200)  // kinda working as a debounce, if the user scrolls too fast we should way a bit.
        launch {
            listState.animateScrollToItem(lastVisibleRecyclerHeaderId.value)
        }
        setItemAsSelected(
            sectionIdClicked = carouselCategories.value[lastVisibleRecyclerHeaderId.value].sectionId,
            carouselCategories = carouselCategories,
            onCarouselChange = setItemAsSelected,
        )
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        state = listState,
    ) {
        item {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xfff5f5f5),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null,
                    tint = Color(0xff141414),
                )
            }
        }

        items(carouselCategories.value) {
            CategoryItem(it, carouselClick)
        }
    }
}

@Composable
private fun CategoryItem(
    headerModel: HeaderUIModel,
    carouselClick: (Int) -> Unit,
) {
    val backgroundColor: Color by animateColorAsState(
        targetValue = if (headerModel.isSelected)
            Color(0xffffebef)
        else
            Color(0xfff5f5f5),
        animationSpec = tween(),
    )
    val textColor: Color by animateColorAsState(
        targetValue = if (headerModel.isSelected)
            Color(0xffcc002c)
        else
            Color(0xff141414),
        animationSpec = tween(),
    )
    Text(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = CircleShape,
            )
            .clip(CircleShape)
            .clickable {
                carouselClick(headerModel.sectionId)
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        text = headerModel.name,
        fontSize = 15.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight(400),
        color = textColor,
    )
}