package com.androidexample.blacktax




import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.androidexample.blacktax.Networking.BlogArticles
import com.androidexample.blacktax.Networking.GetBlogService
import com.androidexample.blacktax.Networking.RecycleDTO
import com.androidexample.blacktax.Networking.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    // Retrofit service.
    val service = RetrofitClientInstance.retrofitInstance?.create(GetBlogService::class.java)
    var enqueueFailed=false
    var enqueueInitialization=false
    var myList = mutableListOf<RecycleDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
        setupListeners()

        //
        // RetrofitClientInstance
        //
        runEnqueue(service, ProjectData.currentPage)

    }


    override fun onSaveInstanceState(outState: Bundle?) {
        // For some reason, this doesn't work if outPersistentState exists!
        super.onSaveInstanceState(outState)
        ProjectData.onSavedState=true
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
    }


    override fun onStop() {
        super.onStop()
    }


    private fun initialize() {
        butPagePrev.text="<"
        butPageNext.text=">"

        // Initially, we don't this button active as there is no page 0.
        if  (!ProjectData.onSavedState) {
            butPagePrev.isEnabled=false
        }
        pageButtonsSaveState()
    }


    private fun setupListeners() {
        butPagePrev.setOnClickListener {
            ProjectData.buttonClicked="prev"

            ProjectData.currentPage--
            if (ProjectData.currentPage==1) {
                butPagePrev.isEnabled = false
                ProjectData.butPrevPageState=false
            } else{
                butPagePrev.isEnabled = true
                ProjectData.butNextPageState=butPageNext.isEnabled
            }
            preparePage(ProjectData.currentPage)
        }

        butPageNext.setOnClickListener {
            // We turn it off until network load is finished.
            ProjectData.buttonClicked="next"
            butPageNext.isEnabled=false

            ProjectData.currentPage++
            if (ProjectData.currentPage==ProjectData.maxPagesAtCompile) {
                butPageNext.isEnabled = false
                ProjectData.butNextPageState=butPageNext.isEnabled
            }
            preparePage(ProjectData.currentPage)
        }
    }


    private fun preparePage(currentPage: Int) {
        myList.clear()
        runEnqueue(service, currentPage)
    }


    private fun runEnqueue(service: GetBlogService?, currentPage: Int = 1)  {
        progressBar1.visibility=View.VISIBLE

        val call = service?.getAllArticles(currentPage.toString())
        call?.enqueue(object : Callback<List<BlogArticles>> {
            override fun onResponse(call: Call<List<BlogArticles>>, response: Response<List<BlogArticles>>) {
                // Retrofit succeeded to get networking and is hitting main url.
                // Retrofit only responds after it gets the data.
                if (response.isSuccessful) {
                    val body = response.body()  // The entire JSON body.
                    var bodyLastIndex = body!!.lastIndex

                    // We're converting the weird JSON output to a standard data class.
                    for (i in 0..bodyLastIndex) {
                        var title = body[i].title.titleRendered
                        var urlLink = body[i].URLLink
                        val date = body[i].date
                        val id = body[i].id
                        val modifiedDate = body[i].modifiedDate
                        var htmlArticle = body[i].content.htmlRendered
                        var imageBlogURL = body[i].imageBlogURL

                        // Strips off some of the html codes that are not displaying correctly.
                        title=parseTitle(title)

                        // Adds to the recycler List DTO.
                        this@MainActivity.myList.add(
                            i,
                            RecycleDTO(title, urlLink, date, id, modifiedDate, htmlArticle, imageBlogURL)
                        )
                    }

                    Log.i("!!!", this@MainActivity.myList[0].title)
                    displayData(this@MainActivity.myList)
                    pageButtonsRestoreState()
                    stopProgressBar()
                } else {
                    // no data in query.
                    var isLastPage=false
                    if (response.code() == 400) {
                        // Thankfully, the recyclerView doesn't fail here.
                        Log.i("!!!", "query is not found in retrofit!!")
                    }
                    pageButtonsRestoreState()
                    stopProgressBar()
                }
            }

            override fun onFailure(call: Call<List<BlogArticles>>, t: Throwable) {
                // No network or cannot get to URL.
                Log.i("!!!", "retrofit failed!")
                stopProgressBar()
            }
        })
    }

    private fun stopProgressBar() {
        progressBar1.visibility=View.INVISIBLE
    }


    private fun parseTitle(title: String): String {
        var titleMod = title

        titleMod=titleMod.replace(" &#8220;", " \"")
        titleMod=titleMod.replace("&#8221;", "\"")
        titleMod=titleMod.replace(" &#8212", "")

        return titleMod
    }



    private fun pageButtonsSaveState() {
         /* Enables the paging buttons...these are the reasons:
            1) If you keep clicking NextPage, it eventually "goes past" page 5 and loads many more records in the List than it should.
            2) The same thing happens with PrevPage.
          */

        // save button sates
        ProjectData.butPrevPageState = butPagePrev.isEnabled
        ProjectData.butNextPageState = butPageNext.isEnabled

    }

    private fun pageButtonsRestoreState() {
        /* Enables the paging buttons...these are the reasons:
           1) If you keep clicking NextPage, it eventually "goes past" page 5 and loads many more records in the List than it should.
           2) The same thing happens with PrevPage.
         */

        // save button states
        if (ProjectData.currentPage > 1) {
            butPagePrev.isEnabled=true
            ProjectData.butPrevPageState=butPagePrev.isEnabled
        }
        if (ProjectData.currentPage < ProjectData.maxPages) {
            butPageNext.isEnabled=true
            ProjectData.butPrevPageState=butPageNext.isEnabled
        }
    }


    fun displayData(list : MutableList<RecycleDTO>) {
        //
        // Call RecyclerView Adapter
        //
        recyclerView.apply {
            val llm = LinearLayoutManager(this@MainActivity)
            val adapter = Adapter(list)
            recyclerView.adapter=adapter
            recyclerView.layoutManager=llm
        }
    }
}
