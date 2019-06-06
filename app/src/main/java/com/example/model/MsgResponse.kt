package com.example.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MsgResponse(
    @SerializedName("content")
    @Expose
    val content: String
)