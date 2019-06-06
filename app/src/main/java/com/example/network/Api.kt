package com.example.network

import com.example.model.MsgModel
import com.example.model.MsgResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("/app/hello")
    fun sendMsg(@Body msg: MsgModel): Single<MsgResponse>
}