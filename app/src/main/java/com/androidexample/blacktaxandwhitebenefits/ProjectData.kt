package com.androidexample.blacktaxandwhitebenefits

object ProjectData {

    const val maxPagesAtCompile = 5               // Base of how many pages we know is valid.
    var maxPages: Int = maxPagesAtCompile

    val putExtra_BlogWebView: String = "EXTRA_BLOGWEBVIEW"
    var currentPage = 1

    var onSavedState = false
    private val htmlTextSizeDefault = 22
    var htmlTextSize = htmlTextSizeDefault

    var butPrevPageState = false
    var butNextPageState = false

    var buttonClicked=""
}


