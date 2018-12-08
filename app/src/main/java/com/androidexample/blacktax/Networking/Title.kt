package com.androidexample.blacktax.Networking

import com.google.gson.annotations.SerializedName

data class Title (
    // Article Title
    @SerializedName("rendered")  var titleRendered: String
)