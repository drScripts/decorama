package com.example.rayjo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailDataPlaceHolder(
        var type : String = "",
        var buyerName : String = "",
        var buyerEmail : String = "",
        var paymentMethod : String = "",
        var date : String = "",
        var profit : String = "",
        var uriProofTransaction : String = "",
) : Parcelable