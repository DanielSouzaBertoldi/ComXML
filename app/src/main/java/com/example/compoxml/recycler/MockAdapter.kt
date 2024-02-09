package com.example.compoxml.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.compoxml.databinding.HeaderBinding
import com.example.compoxml.databinding.MockListItemBinding

private const val HEADER_TYPE = 0
private const val MENU_ITEM_TYPE = 1

class MockAdapter : ListAdapter<MockListUIModel, RecyclerView.ViewHolder>(MockListUIModelDiffCallback) {

    override fun getItemViewType(position: Int) = when(getItem(position)) {
        is MockListUIModel.Header -> HEADER_TYPE
        is MockListUIModel.Item -> MENU_ITEM_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            HEADER_TYPE -> HeaderViewHolder(parent)
            MENU_ITEM_TYPE -> MenuItemViewHolder(parent)
            else -> throw IllegalArgumentException("Unknown viewtype $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is HeaderViewHolder && item is MockListUIModel.Header -> {
                holder.bind(item)
            }
            holder is MenuItemViewHolder && item is MockListUIModel.Item -> {
                holder.bind(item)
            }
        }
    }

    class HeaderViewHolder(private val binding: HeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        constructor(parent: ViewGroup) : this(
            HeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        fun bind(itemData: MockListUIModel.Header) {
            Log.d("RECYCLER-ADAPTER", "Header binded: ${itemData.name}")
            binding.headerTitle.text = itemData.name
        }
    }

    class MenuItemViewHolder(private val binding: MockListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        constructor(parent: ViewGroup) : this(
            MockListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        fun bind(itemData: MockListUIModel.Item) {
            binding.itemName.text = itemData.name
            binding.itemDescription.text = itemData.description
            binding.dishPic.setImageResource(itemData.image)
        }
    }

    object MockListUIModelDiffCallback : DiffUtil.ItemCallback<MockListUIModel>() {
        override fun areItemsTheSame(oldItem: MockListUIModel, newItem: MockListUIModel) =
            when {
                oldItem is MockListUIModel.Header && newItem is MockListUIModel.Header ->
                    oldItem.sectionId == newItem.sectionId
                oldItem is MockListUIModel.Item && newItem is MockListUIModel.Item ->
                    oldItem.id == newItem.id
                else -> oldItem::class == newItem::class
            }

        override fun areContentsTheSame(
            oldItem: MockListUIModel,
            newItem: MockListUIModel
        ) = oldItem == newItem
    }
}