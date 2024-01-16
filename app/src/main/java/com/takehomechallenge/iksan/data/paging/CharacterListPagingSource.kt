package com.takehomechallenge.iksan.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.takehomechallenge.iksan.data.model.CharacterItem
import com.takehomechallenge.iksan.data.retrofit.ApiService

class CharacterListPagingSource(private val apiService: ApiService) : PagingSource<Int, CharacterItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterItem> {
        return try {
            val nextPageNumber = params.key ?: 1
            val data = apiService.getCharacterList(nextPageNumber)
            LoadResult.Page(
                data = data.results ?: emptyList(),
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (nextPageNumber + 1 >= data.info!!.pages) null else nextPageNumber + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}