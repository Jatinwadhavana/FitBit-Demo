package com.demo.fitbit.networks

import com.demo.fitbit.fitbitactivity.model.ActivityModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path

/**
 * FitBit APIs relative urls.
 */
interface ApiInterface {

    @GET("-/activities/{resource-path}/date/{date}/{period}.json")
    fun getActivity(
            @HeaderMap hashMap: HashMap<String, String>,
            @Path("resource-path") type: String,
            @Path("date") date: String,
            @Path("period") period: String
    ): Call<ActivityModel>

    @GET("-/profile.json")
    fun getUserDetails(@HeaderMap hashMap: HashMap<String, String>): Call<ResponseBody>
}