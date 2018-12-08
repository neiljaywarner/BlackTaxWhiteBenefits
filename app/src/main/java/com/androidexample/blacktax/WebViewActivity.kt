package com.androidexample.blacktax

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_webview.*

class WebViewActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        var blogArticleData = intent.getStringArrayListExtra(ProjectData.putExtra_BlogWebView)

        // webViewDataArray array list.
//        webViewDataArray.add(0, ProjectData.myList.get(position).date)
//        webViewDataArray.add(1, ProjectData.myList.get(position).title)
//        webViewDataArray.add(2, ProjectData.myList.get(position).imageBlogURL)
//        webViewDataArray.add(3, ProjectData.myList.get(position).htmlArticle)


        // Load the title.
        var titleData = blogArticleData[1]
        if (titleData.length > 35) {
            titleData=titleData.substring(0,35)
        }
        title=titleData

        // Load the Posted Date
        var postedDate=blogArticleData[0]
        var newDate = postedDate.subSequence(0, postedDate.indexOf("T", ignoreCase = true))
        newDate="Posted Date: " + newDate
        txtWebViewPostedDate.setText(newDate)

        // Load the image
        Picasso.get().load(blogArticleData[2]).into(imgWebView)

        // Lastly, load the webview.
        // Note: WebView just needs the html from JSON...it automatically enters in the HTML header info.
        var htmlContext = blogArticleData[3]
        webview.loadData(htmlContext, "text/html", "UTF-8")
    }
}
