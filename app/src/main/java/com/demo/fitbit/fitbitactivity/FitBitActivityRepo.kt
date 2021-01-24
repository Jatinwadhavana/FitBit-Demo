package com.demo.fitbit.fitbitactivity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.demo.fitbit.networks.ApiInterface
import com.demo.fitbit.networks.RetroClient
import com.demo.fitbit.fitbitactivity.model.ActivitiesCategory
import com.demo.fitbit.fitbitactivity.model.ActivityModel
import com.demo.fitbit.fitbitactivity.model.UserDetails
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
/**
 * FitBit APIs Repository
 */
class FitBitActivityRepo {

    val TAG = javaClass.simpleName

    fun getActivityRepoList(header:HashMap<String, String>,type:String,date:String,period:String): MutableLiveData<MutableList<ActivitiesCategory>>{
        val mutableList: MutableLiveData<MutableList<ActivitiesCategory>> = MutableLiveData()

        val apiInterface = RetroClient.retrofitClient?.create(ApiInterface::class.java)

        apiInterface?.getActivity(header,type,date,period)?.enqueue(object: Callback<ActivityModel?>{

            override fun onResponse(call: Call<ActivityModel?>, response: Response<ActivityModel?>) {
                if(response.isSuccessful){
                    try{
                        Log.d(TAG, "onResponse="+response.body())
                        if(response.body()!=null && response.body()?.activitiesData?.size!!>0 ) {
                            mutableList.value = response.body()?.activitiesData as MutableList<ActivitiesCategory>?
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ActivityModel?>, t: Throwable) {
                Log.e(TAG, "onFailure="+call.toString() )
            }
        })
        return mutableList
    }

    /**
     * User details repository callback.
     */
    fun getUserDetails(header:HashMap<String, String>): MutableLiveData<UserDetails>{
        val mutableList: MutableLiveData<UserDetails> = MutableLiveData()
        val apiInterface = RetroClient.retrofitClient?.create(ApiInterface::class.java)

        apiInterface?.getUserDetails(header)?.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if(response.body()!=null ){
                    if(response.isSuccessful){
                        val userDetailsJson = JSONObject(response.body()?.string()!!)
                        val userDetailsOBJ=UserDetails()
                        Log.d(TAG, "on UserDetails="+userDetailsJson.toString())
                        userDetailsOBJ.userName= userDetailsJson.getJSONObject("user").getString("fullName")
                        mutableList.value=userDetailsOBJ
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "onFailure call="+call.toString())
            }
        })
        return mutableList
    }
}