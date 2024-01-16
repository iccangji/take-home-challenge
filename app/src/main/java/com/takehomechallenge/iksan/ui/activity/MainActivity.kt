package com.takehomechallenge.iksan.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.takehomechallenge.iksan.databinding.ActivityMainBinding
import com.takehomechallenge.iksan.ui.adapter.CharacterAdapter
import com.takehomechallenge.iksan.ui.adapter.LoadingStateAdapter
import com.takehomechallenge.iksan.ui.viewmodel.MainViewModel
import com.takehomechallenge.iksan.ui.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: CharacterAdapter
    private lateinit var footerAdapter: LoadingStateAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CharacterAdapter()
        footerAdapter = LoadingStateAdapter {
            adapter.retry()
        }
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup =  object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount  && footerAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }
        binding.rvCharacter.layoutManager = layoutManager
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            rvCharacter.adapter = adapter.withLoadStateFooter(
                footer = footerAdapter
            )

            viewModel.getCharacterList.observe(this@MainActivity) {
                adapter.submitData(lifecycle, it)
            }

            swipeRefresh.setOnRefreshListener {
                adapter.refresh()
            }

            adapter.addLoadStateListener { loadState ->
                swipeRefresh.isRefreshing = false
                when(loadState.refresh){
                    is LoadState.Loading -> {
                        errorMessage.root.isVisible = false
                        rvCharacter.isVisible = false
                        progressBar.isVisible = true
                    }
                    is LoadState.NotLoading -> {
                        errorMessage.root.isVisible = false
                        progressBar.isVisible = false
                        rvCharacter.isVisible = true
                    }
                    is LoadState.Error -> {
                        progressBar.isVisible = false
                        errorMessage.root.isVisible = true
                        rvCharacter.isVisible = false
                    }
                }
            }

            edSearchBar.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(edSearchBar.text?.isNotEmpty() == true){
                        val intent = Intent(this@MainActivity, SearchActivity::class.java)
                        intent.putExtra(SearchActivity.QUERY, edSearchBar.text.toString())
                        startActivity(intent)
                    }
                    return@setOnEditorActionListener true
                }
                false
            }
            btnFav.setOnClickListener {
                val intent = Intent(it.context, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
    }
}