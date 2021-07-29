package com.example.rayjo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.rayjo.model.DetailDataPlaceHolder
import org.w3c.dom.Text

class DetailTransaction : AppCompatActivity() {

    private lateinit var tvBuyerName : TextView
    private lateinit var tvBuyerEmail : TextView
    private lateinit var tvPaymentMethod : TextView
    private lateinit var tvProfit : TextView
    private lateinit var proofTransaction : ImageView
    private lateinit var btnBack : Button
    private lateinit var detailData : DetailDataPlaceHolder

    companion object{
        const val EXTRA_DETAIL_DATA = "extra_detail_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_detail_transaction)

        tvBuyerName = findViewById(R.id.tv_buyer_name)
        tvBuyerEmail = findViewById(R.id.tv_buyer_email)
        tvPaymentMethod = findViewById(R.id.tv_payment_method)
        tvProfit = findViewById(R.id.tv_profit)
        proofTransaction = findViewById(R.id.proof_transaction)
        btnBack = findViewById(R.id.button_back)

        detailData = intent.getParcelableExtra<DetailDataPlaceHolder>(EXTRA_DETAIL_DATA)!!
        tvBuyerName.text = detailData.buyerName
        tvBuyerEmail.text = detailData.buyerEmail
        tvPaymentMethod.text = detailData.paymentMethod
        tvProfit.text = detailData.profit

        Glide.with(this)
            .load(detailData.uriProofTransaction)
            .into(proofTransaction)

        btnBack.setOnClickListener {
            val back = Intent(this,HistoryTransaction::class.java)
            back.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(back)
        }
    }
}








