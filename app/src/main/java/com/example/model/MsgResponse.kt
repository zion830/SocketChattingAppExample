package com.example.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MsgModel(
    @SerializedName("name")
    @Expose
    val name: String
)