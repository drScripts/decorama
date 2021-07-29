package com.example.rayjo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView

class OrderPlace : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnNetflix : ImageButton
    private lateinit var btnYoutube : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_order_place)

        btnNetflix = findViewById(R.id.netflix_button)
        btnYoutube = findViewById(R.id.youtube_button)

        btnNetflix.setOnClickListener(this)
        btnYoutube.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.youtube_button -> {
                val intent = Intent(this@OrderPlace,PaymentMethodFill::class.java)
                intent.putExtra(PaymentMethodFill.TYPE_EXTRA,"Netflix")
                startActivity(intent)
            }
            R.id.netflix_button -> {
                val intent = Intent(this@OrderPlace,FillFrom::class.java)
                intent.putExtra(FillFrom.TYPE_EXTA,"Youtube")
                startActivity(intent)
            }
        }
    }
}













