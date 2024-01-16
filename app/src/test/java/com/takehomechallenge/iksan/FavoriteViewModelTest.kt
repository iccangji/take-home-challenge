package com.takehomechallenge.iksan

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.takehomechallenge.iksan.data.database.FavoriteCharacterEntity
import com.takehomechallenge.iksan.data.repository.CharacterRepository
import com.takehomechallenge.iksan.data.repository.UiState
import com.takehomechallenge.iksan.ui.viewmodel.FavoriteViewModel
import com.takehomechallenge.iksan.utils.DummyData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoriteViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var characterRepository: CharacterRepository
    private lateinit var viewModel: FavoriteViewModel
    private val dummyList = DummyData.generateFavoriteList()

    @Before
    fun setUp() {
        viewModel = FavoriteViewModel(characterRepository)
    }
    @Test
    fun `when Favorite List Should Not Null then Return Success`() = runTest {
        val expectedResult = MutableLiveData<UiState<List<FavoriteCharacterEntity>>>()
        expectedResult.value = UiState.Success(dummyList)
        `when`(characterRepository.getFavoriteList()).thenReturn(expectedResult)
        val actualResult = viewModel.getFavoriteList().value

        Mockito.verify(characterRepository).getFavoriteList()

        assertNotNull(actualResult)
        assertTrue(actualResult is UiState.Success)
        assertEquals(dummyList.size, (actualResult as UiState.Success).data.size)
    }

    @Test
    fun `when Favorite List Return Null then Return Success`() = runTest {
        val expectedResult = MutableLiveData<UiState<List<FavoriteCharacterEntity>>>()
        expectedResult.value = UiState.Success(emptyList())
        `when`(characterRepository.getFavoriteList()).thenReturn(expectedResult)
        val actualResult = viewModel.getFavoriteList().value

        Mockito.verify(characterRepository).getFavoriteList()

        assertNotNull(actualResult)
        assertTrue(actualResult is UiState.Success)
        assertEquals(0, (actualResult as UiState.Success).data.size)
    }

    @Test
    fun `when deleteFavorite Should Not Exist in Favorite List`() = runTest {
        val favoriteData = MutableLiveData<UiState<List<FavoriteCharacterEntity>>>()
        favoriteData.value = UiState.Success(dummyList)
        `when`(characterRepository.getFavoriteList()).thenReturn(favoriteData)
        viewModel.deleteFavorite(dummyList[0])
        viewModel.deleteFavorite(dummyList[1])

        favoriteData.value = UiState.Success(dummyList.drop(1))
        `when`(characterRepository.getFavoriteList()).thenReturn(favoriteData)
        val actualResult = viewModel.getFavoriteList().value

        assertEquals(dummyList.size - 1, (actualResult as UiState.Success).data.size)
    }
}