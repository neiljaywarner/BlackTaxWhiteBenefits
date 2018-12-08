package com.androidexample.blacktax.Networking

import com.google.gson.annotations.SerializedName

data class WebContent (
    // Html content of article.
    @SerializedName("rendered")  var htmlRendered: String
)