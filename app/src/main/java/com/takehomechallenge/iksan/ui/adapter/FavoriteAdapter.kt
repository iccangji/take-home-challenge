package com.takehomechallenge.iksan.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.takehomechallenge.iksan.data.database.FavoriteCharacterEntity
import com.takehomechallenge.iksan.databinding.ItemCharacterFavoriteBinding
import com.takehomechallenge.iksan.ui.activity.DetailsActivity

class FavoriteAdapter(private val deleteClick: (FavoriteCharacterEntity) -> Unit):
    ListAdapter<FavoriteCharacterEntity, FavoriteAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCharacterFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = getItem(position)
        if (character != null) {
            holder.bind(character, deleteClick)
            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.ID_CHAR, character.id)
                it.context.startActivity(intent)
            }
        }
    }

    class ViewHolder(private val binding: ItemCharacterFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(char: FavoriteCharacterEntity, deleteClick: (FavoriteCharacterEntity) -> Unit) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(char.image)
                    .into(ivChar)
                tvName.text = char.name
                btnFav.setOnClickListener {
                    deleteClick(char)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteCharacterEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteCharacterEntity, newItem: FavoriteCharacterEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavoriteCharacterEntity, newItem: FavoriteCharacterEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}