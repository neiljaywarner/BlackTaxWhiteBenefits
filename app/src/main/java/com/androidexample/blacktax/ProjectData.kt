package com.androidexample.blacktax

object ProjectData {
    var myListRecordNum = -1

    // Note: MAXPAGES IS THE NUMBER OF PAGES IN THE JSON URL...AS OF THE BUILD, THERE ARE ONLY 5.
    val maxPagesAtCompile = 5

    val putExtra_BlogWebView: String = "EXTRA_BLOGWEBVIEW"
    var currentPage = 1
    var maxPage = maxPagesAtCompile
    var onSavedState = false
    val htmlTextSizeDefault = 22
    var htmlTextSize = htmlTextSizeDefault

    //button states
    var butFirstPageState = false
    var butLastPageState = false
    var butPrevPageState = false
    var butNextPageState = false
}

