package com.gulshan.moneyfront.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.gulshan.moneyfront.api.MfApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MfRepository @Inject constructor(private val mfApi: MfApi) {
    fun getAllMfs() =
        Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = 30
            ),
            pagingSourceFactory = { MfPagingSource(mfApi) }
        ).liveData
}