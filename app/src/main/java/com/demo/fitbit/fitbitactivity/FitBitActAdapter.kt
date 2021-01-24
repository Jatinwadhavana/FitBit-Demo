package com.demo.fitbit.fitbitactivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.demo.fitbit.R
import com.demo.fitbit.databinding.ItemActivityDetailsBinding
import com.demo.fitbit.fitbitactivity.model.ActivitiesCategory

/**
 * FitBit Activities list Adapter binned with recycler view.
 */
class FitBitActAdapter : RecyclerView.Adapter<FitBitActAdapter.ViewHolder>() {
    var activitiesList: MutableList<ActivitiesCategory> = ArrayList()

    fun addData(arrList: MutableList<ActivitiesCategory>){
        this.activitiesList.addAll(arrList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, pos: Int):  ViewHolder {
        val binding: ItemActivityDetailsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_activity_details, parent, false)
        return ViewHolder(binding)
    }
    class ViewHolder(private val activityDetailsBinding: ItemActivityDetailsBinding) : RecyclerView.ViewHolder(activityDetailsBinding.root) {
        fun bind(data: Any) {
            activityDetailsBinding.setVariable(BR.model, data)
            activityDetailsBinding.executePendingBindings()
        }
    }
    override fun getItemCount(): Int {
        return activitiesList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.bind(activitiesList[pos])
    }
}