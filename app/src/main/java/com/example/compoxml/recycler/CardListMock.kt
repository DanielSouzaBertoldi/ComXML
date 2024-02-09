package com.example.compoxml.recycler

import androidx.annotation.DrawableRes

data class HeaderUIModel(
    val sectionId: Int,
    val name: String,
    val isSelected: Boolean = false,
)

sealed class MockListUIModel {
    data class Header(
        val sectionId: Int,
        val name: String,
    ) : MockListUIModel()

    data class Item(
        val id: Short,
        val name: String,
        val description: String,
        @DrawableRes val image: Int,
    ) : MockListUIModel()
}

fun createMock() = listOf(
    MockListUIModel.Header(
        sectionId = 1,
        name = "Category 1"
    ),
    MockListUIModel.Item(
        id = 1,
        name = "Item 1",
        description = "Description 1",
        image = android.R.drawable.star_big_on,
    ),
    MockListUIModel.Item(
        id = 2,
        name = "Item 2",
        description = "Description 2",
        image = android.R.drawable.star_big_off,
    ),
    MockListUIModel.Header(
        sectionId = 2,
        name = "Category 2"
    ),
    MockListUIModel.Item(
        id = 3,
        name = "Item 3",
        description = "Description 3",
        image = android.R.drawable.star_big_on,
    ),
    MockListUIModel.Item(
        id = 4,
        name = "Item 4",
        description = "Description 4",
        image = android.R.drawable.star_big_off,
    ),
    MockListUIModel.Header(
        sectionId = 3,
        name = "Category 3"
    ),
    MockListUIModel.Item(
        id = 5,
        name = "Item 5",
        description = "Description 5",
        image = android.R.drawable.star_big_on,
    ),
    MockListUIModel.Item(
        id = 6,
        name = "Item 6",
        description = "Description 6",
        image = android.R.drawable.star_big_off,
    ),
    MockListUIModel.Header(
        sectionId = 4,
        name = "Category 4"
    ),
    MockListUIModel.Item(
        id = 7,
        name = "Item 7",
        description = "Description 7",
        image = android.R.drawable.star_big_on,
    ),
    MockListUIModel.Item(
        id = 8,
        name = "Item 8",
        description = "Description 8",
        image = android.R.drawable.star_big_off,
    ),
    MockListUIModel.Item(
        id = 9,
        name = "Item 9",
        description = "Description 9",
        image = android.R.drawable.star_big_on,
    ),
    MockListUIModel.Header(
        sectionId = 5,
        name = "Category 5"
    ),
    MockListUIModel.Item(
        id = 10,
        name = "Item 10",
        description = "Description 10",
        image = android.R.drawable.star_big_off,
    ),
    MockListUIModel.Header(
        sectionId = 6,
        name = "Category 6"
    ),
    MockListUIModel.Item(
        id = 11,
        name = "Item 11",
        description = "Description 11",
        image = android.R.drawable.star_big_on,
    ),
    MockListUIModel.Item(
        id = 12,
        name = "Item 12",
        description = "Description 12",
        image = android.R.drawable.star_big_off,
    ),
    MockListUIModel.Item(
        id = 13,
        name = "Item 13",
        description = "Description 13",
        image = android.R.drawable.star_big_on,
    ),
    MockListUIModel.Header(
        sectionId = 7,
        name = "Category 7"
    ),
    MockListUIModel.Item(
        id = 14,
        name = "Item 14",
        description = "Description 14",
        image = android.R.drawable.star_big_on,
    ),
    MockListUIModel.Item(
        id = 15,
        name = "Item 15",
        description = "Description 15",
        image = android.R.drawable.star_big_off,
    ),
    MockListUIModel.Item(
        id = 16,
        name = "Item 16",
        description = "Description 16",
        image = android.R.drawable.star_big_on,
    ),
    MockListUIModel.Header(
        sectionId = 8,
        name = "Category 8"
    ),
    MockListUIModel.Item(
        id = 17,
        name = "Item 17",
        description = "Description 17",
        image = android.R.drawable.star_big_on,
    ),
    MockListUIModel.Item(
        id = 18,
        name = "Item 18",
        description = "Description 18",
        image = android.R.drawable.star_big_off,
    ),
    MockListUIModel.Item(
        id = 19,
        name = "Item 19",
        description = "Description 19",
        image = android.R.drawable.star_big_on,
    ),
    MockListUIModel.Header(
        sectionId = 9,
        name = "Category 9"
    ),
    MockListUIModel.Item(
        id = 20,
        name = "Item 20",
        description = "Description 20",
        image = android.R.drawable.star_big_on,
    ),
    MockListUIModel.Item(
        id = 21,
        name = "Item 21",
        description = "Description 21",
        image = android.R.drawable.star_big_off,
    ),
    MockListUIModel.Item(
        id = 22,
        name = "Item 22",
        description = "Description 22",
        image = android.R.drawable.star_big_on,
    ),
)