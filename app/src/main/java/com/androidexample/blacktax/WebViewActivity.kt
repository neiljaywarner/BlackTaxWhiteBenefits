package com.androidexample.blacktax

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebSettings
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_webview.*
import java.text.SimpleDateFormat

class WebViewActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        var blogArticleData = intent.getStringArrayListExtra(ProjectData.putExtra_BlogWebView)

        // webViewDataArray array list.
        //
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

        //
        // Load the Posted Date
        //
        // See also on Dates: https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        // https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html
        // This is the format of the blog: 2018-11-21T
//        var postedDate="2018-11-21T21:10:05"   (YYYY/month//day/T/hour/min/sec
        val postedDate=blogArticleData[0]
//        var modDate = parseDate(postedDate)


//        var newDate = postedDate.subSequence(0, postedDate.indexOf("T", ignoreCase = true))
        var modDate="Posted Date: " + postedDate
        txtWebViewPostedDate.setText(modDate)

        //
        // Load the image
        //
        Picasso.get().load(blogArticleData[2]).into(imgWebView)

        // Lastly, load the webview.
        // Note: WebView just needs the html from JSON...it automatically enters in the HTML header info.
        var htmlContext = blogArticleData[3]
        webview.loadData(htmlContext, "text/html", "UTF-8")


        //
        // Changes Webview HTML text size!!
        //
        val websettings : WebSettings = webview.settings
//        var fontSize: Int = resources.getDimension(R.dimen.htmlTextSize).toInt()
        websettings.setDefaultFontSize(ProjectData.htmlTextSize)
    }



    private fun parseDate(postedDate: String?): String {
        // Format the date is coming in...
        //        var postedDate="2018-11-21T21:10:05"   (YYYY/month//day/T/hour/min/sec
        // Note: This is not acceptable to Java in the SimpleDateFormat method!
//        var modDate=getDateInstance().parse(postedDate).toString()
        var modDate= SimpleDateFormat("MM/dd/yyyy").parse("11/21/2018")
        Log.i("!!!", modDate.toString())
//        var modDate = postedDate.format("MMM/dd/yyyy")
        return ""
    }
}
