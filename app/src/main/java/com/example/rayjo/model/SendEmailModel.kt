package com.example.rayjo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SendEmailModel (
    var emailConstumer : String = "",
    var emailData : String = "",
    var passwordData : String = "",
    var profileData : String = "",
    var pinData : String = "",
        ):Parcelable