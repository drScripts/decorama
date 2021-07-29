package com.example.rayjo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfitModelData (
    var date : String = "",
    var profit : String = "",
        ):Parcelable