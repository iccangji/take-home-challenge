package com.takehomechallenge.iksan.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.takehomechallenge.iksan.data.repository.UiState
import com.takehomechallenge.iksan.databinding.ActivitySearchBinding
import com.takehomechallenge.iksan.ui.adapter.LoadingStateAdapter
import com.takehomechallenge.iksan.ui.adapter.SearchAdapter
import com.takehomechallenge.iksan.ui.viewmodel.SearchViewModel
import com.takehomechallenge.iksan.ui.viewmodel.SearchViewModelFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var adapter: SearchAdapter
    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModels<SearchViewModel> {
        SearchViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.btnUp.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        adapter = SearchAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvCharacter.layoutManager = layoutManager
        setupAction()

    }

    private fun setupAction() {
        binding.apply {
            rvCharacter.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )

            val searchQuery = intent.extras?.getString(QUERY) ?: ""
            viewModel.resultIsEmpty(searchQuery)
            viewModel.searchEmptyState.observe(this@SearchActivity){ uiState ->
                when(uiState){
                    is UiState.Success -> {
                        viewModel.searchResult(uiState.data)
                        viewModel.searchResult(searchQuery).observe(this@SearchActivity) {
                            adapter.submitData(lifecycle, it)
                        }
                        adapter.addLoadStateListener { loadState ->
                            if (loadState.refresh is LoadState.NotLoading) {
                                progressBar.isVisible = false
                                tvNotFound.isVisible = true
                                rvCharacter.isVisible = true
                                tvNotFound.isVisible = false
                            }
                        }
                    }
                    is UiState.Loading -> {
                        errorMessage.root.isVisible = false
                        rvCharacter.isVisible = false
                        progressBar.isVisible = true
                        tvNotFound.isVisible = false
                    }
                    is UiState.Error -> {
                        errorMessage.root.isVisible = true
                        progressBar.isVisible = false
                        rvCharacter.isVisible = false
                    }
                    is UiState.Empty -> {
                        tvNotFound.isVisible = true
                        progressBar.isVisible = false
                        rvCharacter.isVisible = false
                    }
                }
            }
        }
    }
    companion object{
        const val QUERY = "query"
    }
}