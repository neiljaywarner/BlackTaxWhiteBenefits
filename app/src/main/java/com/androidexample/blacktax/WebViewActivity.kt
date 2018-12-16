package com.androidexample.blacktax

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebSettings
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_webview.*
import java.text.SimpleDateFormat

class WebViewActivity: AppCompatActivity() {
    lateinit var titleData: String
    lateinit var modPostedDate: String
    lateinit var modDate: String
    lateinit var urlLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        loadPageData()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuID = item.itemId
        when (menuID) {
            R.id.menuitem_share -> {
                sendBlog(gatherTextMessage())
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun gatherTextMessage(): String {
        // Assembles the string data in which to send via SMS.
        var messageStr=""
        val appTitle = getResources().getString(R.string.app_name)

        messageStr += "From: " + appTitle + ":\n\n" + this.titleData+"\n\n" + this.urlLink

        return messageStr
    }


    private fun sendBlog(smsMessage: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, smsMessage)
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent)
        }
    }



    private fun loadPageData() {
        var blogArticleData = intent.getStringArrayListExtra(ProjectData.putExtra_BlogWebView)

        //
        // Load the title.
        //
        this.titleData = blogArticleData[1]
        val maxStringLength: Int = getResources().getInteger(R.integer.title_maxlength)
        var currentPos=maxStringLength
        lateinit var tempTitle: String

        if (this.titleData.length > maxStringLength) {
            // Ensure the title has a full word instead of cutting off.
            var charStr = this.titleData.substring(maxStringLength - 1, maxStringLength)
            while (charStr != " ") {
                currentPos--
                charStr = this.titleData.substring(currentPos - 1, currentPos)
            }
            tempTitle = this.titleData.substring(0, currentPos) + "..."
        } else {
            tempTitle = this.titleData
        }

        this.titleData  = tempTitle

        // Sets activity title.
        title = tempTitle


        //
        // Blog posted date: This is the format of the blog: 2018-11-21T21:10:05      (YYYY/month//day/T/hour/min/sec
        //
        this.modPostedDate = blogDateConversion(blogArticleData[0])

        this.modDate="Posted Date: " + modPostedDate
        txtWebViewPostedDate.setText(modDate)

        //
        // Load the image
        //
        if (blogArticleData[2] != "") {
            Picasso.get().load(blogArticleData[2]).into(imgWebView)
        } else {
            // Load default image.
            Picasso.get().load(R.drawable.no_image).into(imgWebView)
        }

        // Load the URL Link
        this.urlLink=blogArticleData[4]


        // Lastly, load the webview.
        // Note: WebView just needs the html from JSON...it automatically enters in the HTML header info.
        var htmlContext = blogArticleData[3]
        webview.loadData(htmlContext, "text/html; charset=UTF-8", null)


        //
        // Changes Webview HTML text size!!
        //
        val websettings : WebSettings = webview.settings
        websettings.setDefaultFontSize(ProjectData.htmlTextSize)
    }



    private fun blogDateConversion(s: String): String {
        // This is the format of the blog: 2018-11-21T21:10:05      (YYYY/month//day/T/hour/min/sec

        // Remove the T section.
        var tPos=s.indexOf("T", ignoreCase = true)
        var modifiedDate=s.substring(0,tPos)

        var yearPos=modifiedDate.indexOf("-")
        var year=modifiedDate.substring(0,yearPos)

        var monthPos=modifiedDate.indexOf("-", yearPos+1)
        var month=modifiedDate.substring(yearPos+1,monthPos)

        var dayPos=modifiedDate.indexOf("-", monthPos+1)
        var day=modifiedDate.substring(monthPos+1,modifiedDate.length)

        return month  + "/" + day + "/" + year
    }


    private fun parseDate(postedDate: String?): String {
        // Format the date is coming in...
        //        var postedDate="2018-11-21T21:10:05"   (YYYY/month//day/T/hour/min/sec
        // Note: This is not acceptable to Java in the SimpleDateFormat method!
        var modDate= SimpleDateFormat("MM/dd/yyyy").parse("11/21/2018")
        Log.i("!!!", modDate.toString())
        return ""
    }
}
