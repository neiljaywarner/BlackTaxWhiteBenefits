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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //
        // RetrofitClientInstance
        //

        val service = RetrofitClientInstance.retrofitInstance?.create(GetBlogService::class.java)

        val call = service?.getAllArticles("2")


        call?.enqueue(object : Callback<List<BlogArticles>> {
            override fun onResponse(call: Call<List<BlogArticles>>, response: Response<List<BlogArticles>>) {
                // Retrofit succeeded to get networking and is hitting main url.

                Log.i("!!!", "retrofit succeeded!")
                if (response.isSuccessful) {
                    val body = response.body()  // The entire JSON body.
                    var bodyLastIndex= body!!.lastIndex

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
                        if (imageBlogURL == "") imageBlogURL =
                                "www.nothing2.url"    // point to an unknown URL so Picasso doesn't fail.
                        // Adds to the recycler List DTO.
                        ProjectData.myList.add(i, RecycleDTO(title, urlLink, date, id, modifiedDate, htmlArticle, imageBlogURL))
                    }

                    Log.i("!!!", ProjectData.myList[0].title)
                    displayData(ProjectData.myList)
                } else {
                    // no data in query.
                    if (response.code()==400) {
                        Log.i("!!!", "query is not found in retrofit!!")
                    }
                }
            }

            override fun onFailure(call: Call<List<BlogArticles>>, t: Throwable) {
                // Networking failed or cannog get to URL.
                Log.i("!!!", "retrofit failed!")
            }
        })
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
