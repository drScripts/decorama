package com.example.rayjo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class NetflixDataModel(
        var buyerName : String?  = "",
        var date : String? = "",
        var email : String? = "",
        var pass : String? = "",
        var pin : String? = "",
        var profileNumber : Int? = 0,
        var buyerEmail : String? = "",
        var paymentMethod : String? = "",
        var imageUri : String? = "" ,
        var profit : String? = "",
        var type : String? = "netflix"
        ):Parcelable