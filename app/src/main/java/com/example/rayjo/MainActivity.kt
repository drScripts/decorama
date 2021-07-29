package com.example.rayjo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var username : TextView
    private lateinit var btnOrderPlace : Button
    private lateinit var btnOrderHistory : Button
    private lateinit var btnProfit : Button
    private lateinit var btnLogout : Button

    companion object{
        private val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_main)

        username = findViewById(R.id.username)
        btnOrderPlace = findViewById(R.id.btn_orderPlace)
        btnOrderHistory = findViewById(R.id.btn_orderHistory)
        btnProfit = findViewById(R.id.btn_profit)
        btnLogout = findViewById(R.id.logout)


        username.text = Firebase.auth.currentUser?.displayName.toString()

        btnOrderPlace.setOnClickListener(this)
        btnOrderHistory.setOnClickListener(this)
        btnProfit.setOnClickListener(this)
        btnLogout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_profit -> {
                val intent = Intent(this,ProfitActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_orderHistory -> {
                val intent = Intent(this,HistoryTransaction::class.java)
                startActivity(intent)
            }
            R.id.btn_orderPlace -> {
                val intent = Intent(this@MainActivity,OrderPlace::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                Firebase.auth.signOut()
                val intent  = Intent(this@MainActivity,LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }


}