package com.example.rayjo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayjo.model.DetailDataPlaceHolder
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField

class HistoryTransaction : AppCompatActivity() {

    private lateinit var rvHistory : RecyclerView
    private lateinit var db : FirebaseFirestore
    private lateinit var historyList : ArrayList<DetailDataPlaceHolder>
    private lateinit var historyAdapter : HistoryItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_history_transaction)

        rvHistory = findViewById(R.id.rv_history)
        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.setHasFixedSize(true)

        historyList = arrayListOf()
        historyAdapter = HistoryItemAdapter(historyList,this)
        rvHistory.adapter = historyAdapter

        getDataHistory()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun getDataHistory() {
        db = FirebaseFirestore.getInstance()
        db.collection("youtube")
            .addSnapshotListener { value, error ->
                if(error != null){
                    Toast.makeText(this,"Failed Load Data Youtube From Database",Toast.LENGTH_LONG).show()
                }

                for(dc in value!!.documentChanges){
                    if(dc.type == DocumentChange.Type.ADDED){
                        val dataPlaceHolder = DetailDataPlaceHolder()
                        dataPlaceHolder.buyerEmail = dc.document.getField<String>("buyerEmail").toString()
                        dataPlaceHolder.buyerName = dc.document.getField<String>("buyerName").toString()
                        dataPlaceHolder.date = dc.document.getField<String>("date").toString()
                        dataPlaceHolder.type = dc.document.getField<String>("type").toString()
                        dataPlaceHolder.paymentMethod = dc.document.getField<String>("paymentMethod").toString()
                        dataPlaceHolder.uriProofTransaction = dc.document.getField<String>("imageUri").toString()
                        dataPlaceHolder.profit = dc.document.getField<String>("profit").toString()
                        historyList.add(dataPlaceHolder)
                    }
                }
                historyAdapter.notifyDataSetChanged()
            }
        db.collection("netflix")
            .addSnapshotListener { value, error ->
                if(error != null){
                    Toast.makeText(this,"Failed Load Data Netflix From Database",Toast.LENGTH_LONG).show()
                }

                for(dc in value!!.documentChanges){
                    if(dc.type == DocumentChange.Type.ADDED){
                        val dataPlaceHolder = DetailDataPlaceHolder()
                        dataPlaceHolder.buyerEmail = dc.document.getField<String>("buyerEmail").toString()
                        dataPlaceHolder.buyerName = dc.document.getField<String>("buyerName").toString()
                        dataPlaceHolder.date = dc.document.getField<String>("date").toString()
                        dataPlaceHolder.type = dc.document.getField<String>("type").toString()
                        dataPlaceHolder.paymentMethod = dc.document.getField<String>("paymentMethod").toString()
                        dataPlaceHolder.uriProofTransaction = dc.document.getField<String>("imageUri").toString()
                        dataPlaceHolder.profit = dc.document.getField<String>("profit").toString()
                        historyList.add(dataPlaceHolder)
                    }
                }
                historyAdapter.notifyDataSetChanged()
            }
    }
}