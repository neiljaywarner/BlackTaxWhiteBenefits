package com.androidexample.blacktaxandwhitebenefits.Networking

import com.google.gson.annotations.SerializedName

data class Title (
    // Article Title
    @SerializedName("rendered")  var titleRendered: String
)