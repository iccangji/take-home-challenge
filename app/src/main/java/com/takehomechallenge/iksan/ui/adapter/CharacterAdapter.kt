package com.takehomechallenge.iksan.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.takehomechallenge.iksan.data.model.CharacterItem
import com.takehomechallenge.iksan.databinding.ItemCharacterGridBinding
import com.takehomechallenge.iksan.ui.activity.DetailsActivity

class CharacterAdapter:
    PagingDataAdapter<CharacterItem, CharacterAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCharacterGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = getItem(position)
        if (character != null) {
            holder.bind(character)
            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.ID_CHAR, character.id)
                it.context.startActivity(intent)
            }
        }
    }

    class ViewHolder(private val binding: ItemCharacterGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(char: CharacterItem) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(char.image)
                    .into(ivChar)
                tvName.text = char.name
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CharacterItem>() {
            override fun areItemsTheSame(oldItem: CharacterItem, newItem: CharacterItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CharacterItem, newItem: CharacterItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}