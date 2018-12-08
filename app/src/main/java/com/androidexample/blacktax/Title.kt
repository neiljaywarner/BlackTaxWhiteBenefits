package com.androidexample.blacktax

import com.google.gson.annotations.SerializedName

data class Title (
    // Article Title
    @SerializedName("rendered")  var titleRendered: String
)