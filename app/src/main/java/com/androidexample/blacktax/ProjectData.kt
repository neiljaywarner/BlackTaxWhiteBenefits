package com.androidexample.blacktax

import com.androidexample.blacktax.Networking.RecycleDTO

object ProjectData {
    var myListRecordNum = -1

    // Note: MAXPAGES IS THE NUMBER OF PAGES IN THE JSON URL...AS OF THE BUILD, THERE ARE ONLY 5.
    val maxPagesAtCompile = 5

    val putExtra_BlogWebView: String = "EXTRA_BLOGWEBVIEW"
    var myList = mutableListOf<RecycleDTO>()
    var currentPage = 1
    var maxPage = maxPagesAtCompile
}

