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

// ToDo: Bug: when you go to the last page, then rotate it then open the item.  It always shows up in protrait mode.
// ToDo: Bug: when you go to the last page, then rotate it then rotate it back, the next page button is now lit up!  But its at the last page, so it shouldn't be lit up!



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
        butFirstPage.text="<<"
        butPagePrev.text="<"
        butPageNext.text=">"
        butLastPage.text=">>"

        // initially, we don't this button active as there is no page 0.
        if  (!ProjectData.onSavedState) {
            butPagePrev.isEnabled=false
        }


//        pageButtonsSaveState(true)


        //ToDo: To truly find out the last page at runtime, then we need a coroutine listener or something.
//        // loop thru pages to see what is the last page on the URL.
//        enqueueInitialization=true
//        var currentURLPage = ProjectData.maxPagesAtCompile
//        while (enqueueFailed==false) {
//            runEnqueue(service, currentURLPage)
//            currentURLPage++
//        }
//
//        enqueueInitialization=false
    }


    private fun setupListeners() {
        butPagePrev.setOnClickListener {
            ProjectData.currentPage--
            butPageNext.isEnabled = true
            if (ProjectData.currentPage==1) {
                butPagePrev.isEnabled = false
            }
            myList.clear()
            runEnqueue(service, ProjectData.currentPage)
        }

        butPageNext.setOnClickListener {
            ProjectData.currentPage++
            if (ProjectData.currentPage==ProjectData.maxPagesAtCompile) {
                butPageNext.isEnabled = false
            }
            butPagePrev.isEnabled=true
            myList.clear()
            runEnqueue(service, ProjectData.currentPage)
        }

        butFirstPage.setOnClickListener {
            ProjectData.currentPage=1
            butPagePrev.isEnabled=false
            butPageNext.isEnabled=true
            myList.clear()
            runEnqueue(service, ProjectData.currentPage)
        }

        butLastPage.setOnClickListener {
            ProjectData.currentPage=ProjectData.maxPagesAtCompile
            butPageNext.isEnabled=false
            butPagePrev.isEnabled=true
            myList.clear()
            runEnqueue(service, ProjectData.currentPage)
        }
    }


    private fun runEnqueue(service: GetBlogService?, currentPage: Int = 1) {
        progressBar1.visibility=View.VISIBLE

        val call = service?.getAllArticles(currentPage.toString())
        call?.enqueue(object : Callback<List<BlogArticles>> {
            override fun onResponse(call: Call<List<BlogArticles>>, response: Response<List<BlogArticles>>) {
                // Retrofit succeeded to get networking and is hitting main url.

                Log.i("!!!", "retrofit succeeded!")
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
//                    pageButtonsSaveState(false)
                    stopProgressBar()

                } else {
                    // no data in query.
                    if (response.code() == 400) {
                        // Thankfully, the recyclerView doesn't fail here.
                        Log.i("!!!", "query is not found in retrofit!!")
                        hitLastPage()
//                        pageButtonsSaveState(false)
                    }
                    stopProgressBar()
                }
            }

            override fun onFailure(call: Call<List<BlogArticles>>, t: Throwable) {
                // Networking failed or cannog get to URL.
                Log.i("!!!", "retrofit failed!")
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



    private fun pageButtonsSaveState(initial: Boolean = true) {
        // Enables the paging buttons..the reason I have to do this is that if enabled, before Retrofit loads, if
        //   fails to open the correct page.

        if (initial) {
            // save button sates
            ProjectData.butFirstPageState = butFirstPage.isEnabled
            ProjectData.butLastPageState = butLastPage.isEnabled
            ProjectData.butPrevPageState = butPagePrev.isEnabled
            ProjectData.butNextPageState = butPageNext.isEnabled

            // now disable them until Retrofit enqueue is completed.
            butFirstPage.isEnabled=false
            butLastPage.isEnabled=false
            butPagePrev.isEnabled=false
            butPageNext.isEnabled=false
        } else {
            butFirstPage.isEnabled=ProjectData.butFirstPageState
            butLastPage.isEnabled=ProjectData.butLastPageState
            butPagePrev.isEnabled=ProjectData.butPrevPageState
            butPageNext.isEnabled=ProjectData.butNextPageState
        }
    }


    private fun hitLastPage() {
        if (enqueueInitialization) {
            //  Determining last page...user didn't do this.
            ProjectData.maxPage=ProjectData.currentPage - 1
        } else {
            ProjectData.currentPage--
            butPageNext.isEnabled=false
            ProjectData.butNextPageState=butPageNext.isEnabled
        }
    }

    fun displayData(list : MutableList<RecycleDTO>) {
        //
        // RecyclerView
        //
        recyclerView.apply {
            val llm = LinearLayoutManager(this@MainActivity)
            val adapter = Adapter(list)
            recyclerView.adapter=adapter
            recyclerView.layoutManager=llm
        }
    }
}
