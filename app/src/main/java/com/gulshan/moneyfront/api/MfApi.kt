package com.gulshan.moneyfront.api

import retrofit2.http.*

interface MfApi {
    companion object{
        const val BASE_URL = "https://61e43a7dfbee6800175eb271.mockapi.io/"
    }

    @Headers("Accept: application/json")
    @GET("mf/schemes/{page_num}")
    suspend fun getAllMfs(
        @Path("page_num") page:Int,
    ):MfResponse
}