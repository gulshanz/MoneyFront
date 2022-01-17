package com.gulshan.moneyfront.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gulshan.moneyfront.api.MfApi
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 1

class MfPagingSource(
    private val mfApi: MfApi,
) : PagingSource<Int, Mf>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Mf> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = mfApi.getAllMfs(position)
            val mfs = response.List
            LoadResult.Page(
                data = mfs,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (mfs.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Mf>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPageIndex = state.pages.indexOf(state.closestPageToPosition(anchorPosition))
            state.pages.getOrNull(anchorPageIndex + 1)?.prevKey ?: state.pages.getOrNull(
                anchorPageIndex - 1
            )?.nextKey
        }
    }
}