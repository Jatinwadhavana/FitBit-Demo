package com.demo.fitbit.fitbitactivity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.fitbit.BR
import com.demo.fitbit.BaseActivity
import com.demo.fitbit.MyApplication
import com.demo.fitbit.R
import com.demo.fitbit.databinding.ActivityListMainBinding
import com.demo.fitbit.fitbitactivity.model.ActivitiesCategory
import com.demo.fitbit.fitbitactivity.model.UserDetails
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_fitbit_login.*
import kotlinx.android.synthetic.main.activity_fitbit_login.view.*
import kotlinx.android.synthetic.main.activity_list_main.*
import kotlinx.android.synthetic.main.activity_list_main.toolbar
import kotlinx.android.synthetic.main.activity_list_main.view.*
import kotlinx.android.synthetic.main.activity_list_main.view.tvTitle
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class FitBitActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {
    private val TAG = javaClass.simpleName
    private lateinit var fitBitActAdapter: FitBitActAdapter
    private lateinit var binding: ActivityListMainBinding
    private val fitBitActViewModel = FitBitActViewModel()

    private var dayOfMonth:Int=0
    private var monthOfYear:Int=0
    private var year:Int=0
    private val now = Calendar.getInstance()
    private val formatter = DecimalFormat("00")

    private lateinit var layoutManager : LinearLayoutManager

    private var canCallPaging = true
    private var pastVisibleItems = 0
    private var visibleItemCount:Int = 0
    private var totalItemCount:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_list_main)
        initUI()

        if(year == 0){
            year=now[Calendar.YEAR]
            monthOfYear=now[Calendar.MONTH]
            dayOfMonth=now[Calendar.DAY_OF_MONTH]
        }

        toolbar.tvTitle.text=getString(R.string.ftb_act)
        setToolBar(toolbar)

        if (intent?.dataString != null) {
            //Intent will have data once user login into fitBit account from browser
            Log.d(TAG, "=" + intent?.dataString)
            fitBitActViewModel.getDataFromIntent(intent)
            if(MyApplication.instance?.isNetworkAvailable == true){
                callingActivityAPI("1m", getFormattedDate())
                fitBitActViewModel.getUserLiveData().observe(this, object : Observer<UserDetails> {
                    override fun onChanged(t: UserDetails?) {
                        binding.setVariable(BR.userDetails, t)
                        binding.executePendingBindings()
                    }
                })
            }else{
                Toast.makeText(this, getString(R.string.err_internet), Toast.LENGTH_LONG).show()
            }
        } else {
            //Else
            fitBitActViewModel.openFitBitLoginBrowser(this)
        }
        binding.swipeToRefresh.setOnRefreshListener {
            if(MyApplication.instance?.isNetworkAvailable == true){
                fitBitActViewModel.mutableActivityListLiveData?.removeObserver(activitiesObserver)
                fitBitActViewModel.mutableActivityListLiveData=null
                fitBitActAdapter.activitiesList.clear()
                fitBitActAdapter.notifyDataSetChanged()
                callingActivityAPI("1m", getFormattedDate())
            }else{
                Toast.makeText(this, getString(R.string.err_internet), Toast.LENGTH_LONG).show()
            }
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if (canCallPaging) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            canCallPaging = false
                            fitBitActViewModel.mutableActivityListLiveData?.removeObserver(activitiesObserver)
                            fitBitActViewModel.mutableActivityListLiveData = null

                            val lastListDate=fitBitActAdapter.activitiesList[fitBitActAdapter.activitiesList.size - 1].dateTime
                            val cal = Calendar.getInstance()
                            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                            cal.time = sdf.parse(lastListDate)!!
                            cal.add(Calendar.DATE,-1)
                            callingActivityAPI("1m", sdf.format(cal.time))
                        }
                    }
                }
            }
        })
    }

    /**
     * UI setup
     */
    private fun initUI(){
        binding.recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.itemAnimator = DefaultItemAnimator()

        fitBitActAdapter = FitBitActAdapter()
        binding.recyclerView.adapter = fitBitActAdapter
    }

    /**
     * Activities list observer
     * Observer for data callback received and can be update the UI.
     */
    private var activitiesObserver: Observer<MutableList<ActivitiesCategory>> = Observer<MutableList<ActivitiesCategory>> { t ->
        progressBar.visibility = View.GONE
        fitBitActAdapter.addData(t?.reversed()?.toMutableList()!!)
        fitBitActAdapter.notifyDataSetChanged()
        binding.swipeToRefresh.isRefreshing = false
        canCallPaging = true //Pagination
    }

    private fun callingActivityAPI(period: String, date: String) {
        progressBar.visibility = View.VISIBLE
        fitBitActViewModel.getActivityLiveData("distance", date, period).observe(this, activitiesObserver)
    }

    private fun getFormattedDate(): String {
        return "$year"+"-"+formatter.format((monthOfYear + 1)).toString()+"-"+formatter.format(dayOfMonth).toString()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        var filter: MenuItem? = menu.findItem(R.id.action_filter)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                finish()
            R.id.action_filter -> {
                val dpd: DatePickerDialog = DatePickerDialog.newInstance(this@FitBitActivity, year, monthOfYear, dayOfMonth)
                dpd.show(supportFragmentManager, "Filter Date")
                dpd.accentColor = resources.getColor(R.color.colorPrimary)
                dpd.version = DatePickerDialog.Version.VERSION_2
                dpd.maxDate = now
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        this.dayOfMonth = dayOfMonth
        this.monthOfYear = monthOfYear
        this.year=year
        if(MyApplication.instance?.isNetworkAvailable == true){
            fitBitActViewModel.mutableActivityListLiveData?.removeObserver(activitiesObserver)
            fitBitActViewModel.mutableActivityListLiveData = null
            fitBitActAdapter.activitiesList.clear()
            fitBitActAdapter.notifyDataSetChanged()
            callingActivityAPI("1m", getFormattedDate())
        }else{
            Toast.makeText(this, getString(R.string.err_internet), Toast.LENGTH_LONG).show()
        }
    }
}