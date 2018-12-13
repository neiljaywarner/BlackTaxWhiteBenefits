package com.androidexample.blacktax

object ProjectData {
    var myListRecordNum = -1

    val maxPagesAtCompile = 5               // Base of how many pages we know is valid.
    var maxPages: Int = maxPagesAtCompile

    val putExtra_BlogWebView: String = "EXTRA_BLOGWEBVIEW"
    var currentPage = 1

    var onSavedState = false
    val htmlTextSizeDefault = 22
    var htmlTextSize = htmlTextSizeDefault

    //button states
    var butFirstPageState = false
    var butLastPageState = false
    var butPrevPageState = false
    var butNextPageState = false

    var buttonClicked=""
}


