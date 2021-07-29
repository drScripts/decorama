package com.example.rayjo.services

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Services {

    fun checkCredential ():Boolean {
        return Firebase.auth.currentUser != null
    }
}