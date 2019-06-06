package com.example.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MsgModel {

    @SerializedName("name")
    @Expose
    var name: String? = null

}