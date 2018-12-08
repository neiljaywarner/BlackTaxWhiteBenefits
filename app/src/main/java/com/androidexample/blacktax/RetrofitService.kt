package com.androidexample.blacktax

import retrofit2.Call
import retrofit2.http.GET

interface GetBlogService {

    // Gets everything after the base URL.
    @GET("/wp-json/wp/v2/posts?_embed")

    // Calls top-level object 'BlogArticles'.
    fun getAllArticles(): Call<List<BlogArticles>>
}
