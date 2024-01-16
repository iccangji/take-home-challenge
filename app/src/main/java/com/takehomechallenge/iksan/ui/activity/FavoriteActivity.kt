package com.takehomechallenge.iksan.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.takehomechallenge.iksan.R
import com.takehomechallenge.iksan.data.repository.UiState
import com.takehomechallenge.iksan.databinding.ActivityFavoriteBinding
import com.takehomechallenge.iksan.ui.adapter.FavoriteAdapter
import com.takehomechallenge.iksan.ui.viewmodel.FavoriteViewModel
import com.takehomechallenge.iksan.ui.viewmodel.FavoriteViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private val viewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.btnUp.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager
        setupAction()
    }

    private fun setupAction(){
        binding.apply {
            adapter = FavoriteAdapter{
                viewModel.deleteFavorite(it)
                Snackbar.make(root, getString(R.string.favorite_delete),Snackbar.LENGTH_SHORT).show()
            }
            rvFavorite.adapter = adapter
            viewModel.getFavoriteList().observe(this@FavoriteActivity) { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        uiState.data.apply {
                            progressBar.isVisible = false
                            tvFavEmpty.isVisible = isEmpty()
                            rvFavorite.isVisible = isNotEmpty()
                            adapter.submitList(uiState.data)
                        }
                    }

                    is UiState.Loading -> {
                        rvFavorite.isVisible = false
                        progressBar.isVisible = true
                        tvFavEmpty.isVisible = false
                    }

                    is UiState.Error -> {
                        rvFavorite.isVisible = false
                        progressBar.isVisible = false
                        tvFavEmpty.isVisible = false
                        Snackbar.make(root, getString(R.string.favorite_problem),Snackbar.LENGTH_SHORT).show()
                    }

                    is UiState.Empty -> {}
                }
            }
        }
    }
}