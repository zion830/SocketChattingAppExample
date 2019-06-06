package com.example.network

import com.example.model.MsgModel
import com.example.model.MsgResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @POST("/app/hello")
    fun sendMsg(@Query("msg") msg: MsgModel): Single<MsgResponse>
}