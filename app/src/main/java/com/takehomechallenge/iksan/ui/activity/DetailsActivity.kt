package com.takehomechallenge.iksan.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.takehomechallenge.iksan.R
import com.takehomechallenge.iksan.data.repository.UiState
import com.takehomechallenge.iksan.databinding.ActivityDetailsBinding
import com.takehomechallenge.iksan.ui.viewmodel.DetailsViewModel
import com.takehomechallenge.iksan.ui.viewmodel.DetailsViewModelFactory

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val viewModel by viewModels<DetailsViewModel> {
        DetailsViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.btnUp.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val idCharacter = intent.extras?.getInt(ID_CHAR) ?: 0
        viewModel.getCharacterDetails(idCharacter)
        viewModel.uiState.observe(this@DetailsActivity) { uiState ->
            binding.apply {
                when(uiState){
                    is UiState.Loading -> {
                        layoutDetails.isVisible = false
                        progressBar.isVisible = true
                        errorMessage.root.isVisible = false
                    }
                    is UiState.Success -> {
                        progressBar.isVisible = false
                        errorMessage.root.isVisible = false
                        layoutDetails.isVisible = true
                        uiState.apply {
                            tvToolbarName.text = data.name
                            tvName.text = data.name
                            chipSpecies.text = data.species
                            ivGender.setImageResource(
                                when(data.gender){
                                    "Male" -> R.drawable.baseline_male_24
                                    "Female" -> R.drawable.baseline_female_24
                                    else -> R.drawable.baseline_question_mark_24
                                }
                            )
                            tvOrigin.text = data.origin.name
                            tvLocation.text = data.location.name
                            Glide.with(root)
                                .load(data.image)
                                .into(ivAvatar)
                            btnFav.apply {
                                setImageResource(
                                    if (data.isFavorite) R.drawable.baseline_favorite_24
                                    else R.drawable.baseline_favorite_border_24
                                )
                                setOnClickListener{
                                    if (data.isFavorite) {
                                        viewModel.deleteFavorite(data.id)
                                        Snackbar.make(root, getString(R.string.favorite_delete),
                                            Snackbar.LENGTH_SHORT).show()
                                    }
                                    else {
                                        viewModel.addFavorite(data, System.currentTimeMillis())
                                        Snackbar.make(root, getString(R.string.favorite_insert),
                                            Snackbar.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                    is UiState.Error -> {
                        layoutDetails.isVisible = false
                        progressBar.isVisible = false
                        errorMessage.root.isVisible = true
                    }

                    else -> {}
                }
            }

        }
    }
    companion object {
        const val ID_CHAR = "id"
    }
}