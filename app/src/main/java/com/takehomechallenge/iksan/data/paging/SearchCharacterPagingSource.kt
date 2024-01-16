package com.takehomechallenge.iksan.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.takehomechallenge.iksan.data.model.CharacterItem
import com.takehomechallenge.iksan.data.retrofit.ApiService

class SearchCharacterPagingSource(private val apiService: ApiService, private val query: String) : PagingSource<Int, CharacterItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterItem> {
        return try {
            val nextPageNumber = params.key ?: 1
            val data = apiService.searchCharacter(query, nextPageNumber)
            LoadResult.Page(
                data = data.results ?: emptyList(),
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (nextPageNumber + 1 >= (data.info?.pages ?: 0)) null else nextPageNumber + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}