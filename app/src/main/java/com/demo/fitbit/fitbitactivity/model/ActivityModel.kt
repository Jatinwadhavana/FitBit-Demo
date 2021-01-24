package com.demo.fitbit.fitbitactivity.model

import com.google.gson.annotations.SerializedName

data class ActivityModel(
    @SerializedName(
        value = "activities-calories",
        alternate = ["activities-distance", "activities-steps", "body-weight", "body-fat"]
    )
    val activitiesData: List<ActivitiesCategory>
)