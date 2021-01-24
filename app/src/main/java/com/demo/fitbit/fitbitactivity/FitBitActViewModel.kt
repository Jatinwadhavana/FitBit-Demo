package com.demo.fitbit.fitbitactivity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.fitbit.R
import com.demo.fitbit.fitbitactivity.model.ActivitiesCategory
import com.demo.fitbit.fitbitactivity.model.UserDetails
import java.util.HashMap

/**
 * FitBit Activities screen View Model
 */
open class FitBitActViewModel : ViewModel() {
    lateinit var fitBitUserAccess: String
    var holidayRepoKotlin: FitBitActivityRepo? = null
    var mutableActivityListLiveData: MutableLiveData<MutableList<ActivitiesCategory>>? = null
    var mutableUserDetailsLiveData: MutableLiveData<UserDetails>? = null

    init {
        holidayRepoKotlin = FitBitActivityRepo()
    }

    fun getActivityLiveData(type:String,date:String,period:String): LiveData<MutableList<ActivitiesCategory>> {
        if (mutableActivityListLiveData == null) {
            mutableActivityListLiveData = holidayRepoKotlin!!.getActivityRepoList(getHeader(),type,date,period)
        }
        return mutableActivityListLiveData!!
    }
    fun getUserLiveData(): LiveData<UserDetails> {
        if (mutableUserDetailsLiveData == null) {
            mutableUserDetailsLiveData = holidayRepoKotlin!!.getUserDetails(getHeader())
        }
        return mutableUserDetailsLiveData!!
    }

    private fun getHeader(): HashMap<String, String> {
        val header=HashMap<String, String>()
        if(::fitBitUserAccess.isInitialized){
            header["Authorization"] = "Bearer $fitBitUserAccess"
            return header
        }
        header["Authorization"] = "Bearer "
        return header
    }

    internal fun getDataFromIntent(intent: Intent?) {
        var strUserData = "";
        val data = intent?.dataString
        if (data != null) {
            Log.d("getDataNewIntent ==>", data)
            strUserData = data
            try {
                Log.d("strUserData=", strUserData)

                val accessToken = "access_token="
                val userId = "&user_id"
                val expiresIn = "&expires_in="
                val tokenType = "&token_type="

                val accessTokenIndex = strUserData.indexOf(accessToken)
                val userIdIndex = strUserData.indexOf(userId)
                val expiresInIndex = strUserData.indexOf(expiresIn)
                val tokenTypeIndex = strUserData.indexOf(tokenType)

                val strAccessToken = strUserData.substring(accessTokenIndex + accessToken.length, userIdIndex)
                Log.d("strAccessToken=" , strAccessToken)
                fitBitUserAccess=strAccessToken
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    internal fun openFitBitLoginBrowser(activity: Context) {
        val ai = activity.packageManager.getApplicationInfo(activity.packageName, PackageManager.GET_META_DATA)
        val bundle = ai.metaData
        val clientId = bundle.getString("com.fitbit.appID.CLIENT_ID")

        val url = ("https://www.fitbit.com/oauth2/authorize?response_type=token&client_id="
                + clientId
                + "&redirect_uri=" + activity.getString(R.string.str_fitbit_redirect_url)
                + "&scope="
                + activity.getString(R.string.str_fitbit_data_scope)
                + "&expire_in="
                + 31536000)

        Log.d("FITBIT URL ==>", url)
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(url)
        activity.startActivity(openURL)
    }
}