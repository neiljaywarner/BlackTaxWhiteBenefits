package com.androidexample.blacktax




import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.androidexample.blacktax.Networking.BlogArticles
import com.androidexample.blacktax.Networking.GetBlogService
import com.androidexample.blacktax.Networking.RecycleDTO
import com.androidexample.blacktax.Networking.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ToDo: Bug: For some reason, if you go to the last page then rotate the screen, you get multiple duplicate items
//   showing up for some strange reason....only on last page.


class MainActivity : AppCompatActivity() {

    // Retrofit service.
    val service = RetrofitClientInstance.retrofitInstance?.create(GetBlogService::class.java)
    var enqueueFailed=false
    var enqueueInitialization=false

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

        // Clears out the contents of this.
        ProjectData.myList.clear()
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
            ProjectData.myList.clear()
            runEnqueue(service, ProjectData.currentPage)
        }

        butPageNext.setOnClickListener {
            ProjectData.currentPage++
            if (ProjectData.currentPage==ProjectData.maxPagesAtCompile) {
                butPageNext.isEnabled = false
            }
            butPagePrev.isEnabled=true
            ProjectData.myList.clear()
            runEnqueue(service, ProjectData.currentPage)
        }

        butFirstPage.setOnClickListener {
            ProjectData.currentPage=1
            butPagePrev.isEnabled=false
            butPageNext.isEnabled=true
            ProjectData.myList.clear()
            runEnqueue(service, ProjectData.currentPage)
        }

        butLastPage.setOnClickListener {
            ProjectData.currentPage=ProjectData.maxPagesAtCompile
            butPageNext.isEnabled=false
            butPagePrev.isEnabled=true
            ProjectData.myList.clear()
            runEnqueue(service, ProjectData.currentPage)
        }
    }


    private fun runEnqueue(service: GetBlogService?, currentPage: Int = 1) {
        val call = service?.getAllArticles(currentPage.toString())
//        val call = service?.getAllArticles("2")
        call?.enqueue(object : Callback<List<BlogArticles>> {
            override fun onResponse(call: Call<List<BlogArticles>>, response: Response<List<BlogArticles>>) {
                // Retrofit succeeded to get networking and is hitting main url.

                Log.i("!!!", "retrofit succeeded!")
                if (response.isSuccessful) {
                    val body = response.body()  // The entire JSON body.
                    var bodyLastIndex = body!!.lastIndex

                    // We're converting the weird JSON output to a standard data class.
                    for (i in 0..bodyLastIndex) {
                        val title = body[i].title.titleRendered ?: " "
                        var urlLink = body[i].URLLink
                        val date = body[i].date
                        val id = body[i].id
                        val modifiedDate = body[i].modifiedDate
                        var htmlArticle = body[i].content.htmlRendered
                        var imageBlogURL = body[i].imageBlogURL

                        // Some condition checks:
//                        if (imageBlogURL == "") imageBlogURL =
//                                "www.nothing2.url"    // point to an unknown URL so Picasso doesn't fail.
                        // Adds to the recycler List DTO.
                        ProjectData.myList.add(
                            i,
                            RecycleDTO(title, urlLink, date, id, modifiedDate, htmlArticle, imageBlogURL)
                        )
                    }

                    Log.i("!!!", ProjectData.myList[0].title)
                    displayData(ProjectData.myList)
                } else {
                    // no data in query.
                    if (response.code() == 400) {
                        // Thankfully, the recyclerView doesn't fail here.
                        Log.i("!!!", "query is not found in retrofit!!")
                        hitLastPage()
                    }
                }
            }

            override fun onFailure(call: Call<List<BlogArticles>>, t: Throwable) {
                // Networking failed or cannog get to URL.
                Log.i("!!!", "retrofit failed!")
            }
        })
    }

    private fun hitLastPage() {
        if (enqueueInitialization) {
            //  Determining last page...user didn't do this.
            ProjectData.maxPage=ProjectData.currentPage - 1
        } else {
            ProjectData.currentPage--
            butPageNext.isEnabled=false
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
